package com.tma.coffeehouse.Cart;

import com.tma.coffeehouse.Cart.DTO.CartDTO;
import com.tma.coffeehouse.Cart.DTO.CheckOutInfoDTO;
import com.tma.coffeehouse.Cart.DTO.GetFullCartDTO;
import com.tma.coffeehouse.Cart.Mapper.CartMapper;
import com.tma.coffeehouse.CartDetails.CartDetail;
import com.tma.coffeehouse.CartDetails.CartDetailRepository;
import com.tma.coffeehouse.CartDetails.CartDetailService;
import com.tma.coffeehouse.CartDetails.DTO.AddCartDetailDTO;
import com.tma.coffeehouse.CartDetails.DTO.DetailOfCartDTO;
import com.tma.coffeehouse.CartDetails.Mapper.AddCartDetailMapper;
import com.tma.coffeehouse.CartDetails.Mapper.DetailOfCartMapper;
import com.tma.coffeehouse.Customers.Customer;
import com.tma.coffeehouse.Customers.CustomerService;
import com.tma.coffeehouse.ExceptionHandling.CustomException;
import com.tma.coffeehouse.Order.Order;
import com.tma.coffeehouse.Order.OrderRepository;
import com.tma.coffeehouse.Order.OrderStatus;
import com.tma.coffeehouse.OrderDetails.OrderDetail;
import com.tma.coffeehouse.OrderDetails.OrderDetailRepository;
import com.tma.coffeehouse.Product.Product;
import com.tma.coffeehouse.Topping.Topping;
import com.tma.coffeehouse.Utils.CustomUtils;
import com.tma.coffeehouse.Voucher.Voucher;
import com.tma.coffeehouse.Voucher.VoucherRepository;
import com.tma.coffeehouse.config.Cache.CacheService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final VoucherRepository voucherRepository;
    private final CartDetailService cartDetailService;
    private final CartMapper cartMapper;
    private final AddCartDetailMapper addCartDetailMapper;
    private final DetailOfCartMapper detailOfCartMapper;
    private final CustomerService customerService;
    private final CacheService cacheService;
    private final CustomUtils customUtils;
    public CartDTO insert(Cart cart){
        return cartMapper.modelTODto(
                cartRepository.save(cart)
        );
    }
    public CartDTO update(Long customerId, Cart newCart){
        Cart current = cartRepository.findByCustomerId(customerId);
        current.setNote(newCart.getNote());
        Cart saved = cartRepository.save(current);
        return cartMapper.modelTODto(saved);
    }
    public boolean isVoucherValid(GetFullCartDTO cart, Long voucherId){
        Voucher voucher = voucherRepository.findById(voucherId).orElseThrow(
                () -> new CustomException("Không tìm thấy voucher có mã là " + voucherId, HttpStatus.NOT_FOUND)
        );
        System.out.println(voucher.getStartDate());
        System.out.println(voucher.getEndDate());
        if (voucher.getRemainingNumber() < 0 || voucher.getStartDate().after(new Date())
                || voucher.getEndDate().before(new Date())) return false;
        if (cart == null)return true;
        if (cart.getTongtien() < voucher.getMinOrderTotal()) return false;
        Set<Product> productsOfCart = cart.getDetails().stream()
                .map((DetailOfCartDTO::getProduct)).collect(Collectors.toSet());
        for(Product product: voucher.getProducts()){
            if (!productsOfCart.contains(product)){
                return false;
            }
        }
        return true;
    }

    @Transactional(rollbackOn = {Exception.class, Throwable.class} )
    public Order checkOutCart(Long customerId, CheckOutInfoDTO checkOutInfoDTO){
        GetFullCartDTO cart = this.findOne(customerId);
        System.out.println(cart.getVoucher());
        if (checkOutInfoDTO.getVoucherId() != null){
            Voucher voucher =  voucherRepository.findById(checkOutInfoDTO.getVoucherId()).orElseThrow(
                    () -> new CustomException("Không tìm thấy voucher có mã này!", HttpStatus.NOT_FOUND)
            );
            cart.setVoucher(voucher);
        }
        if (checkOutInfoDTO.getVoucherId() != null && !isVoucherValid(cart, checkOutInfoDTO.getVoucherId())){
            throw new CustomException("Voucher không còn hợp lệ!", HttpStatus.BAD_REQUEST);
        }
        String bodyMail = "Chào bạn, cảm ơn đã đặt một đơn hàng tại the Coffee House, chi tiết đơn hàng: \n";
        Order.OrderBuilder newOrder = Order.builder();
        if (cart.getVoucher() != null){
            this.addVoucher(customerId, checkOutInfoDTO.getVoucherId());
            Voucher voucher = cart.getVoucher();
            voucher.setRemainingNumber(voucher.getRemainingNumber() - 1);
            Voucher savedVoucher = voucherRepository.save(voucher);
            newOrder.voucher(savedVoucher);
            bodyMail += "   - Áp dụng voucher giảm " + cart.getVoucher().getPercentage()* 100 + "% cho đơn hàng này.\n";
        }
        newOrder.status(OrderStatus.RECEIVED);
        newOrder.tongsl(this.calculateCartTotalAmount(cart));
        Long[] result = this.calculateCartTotal(cart);
        newOrder.tongtien(result[0]);

        bodyMail += "   - Tổng tiền: " + result[0] + "đ.\n";
        bodyMail += "   - Giảm giá: " + result[1] + "đ.\n";
        bodyMail += "   - Địa chỉ: " + checkOutInfoDTO.getAddress() + ".\n";
        bodyMail += "   - Thời gian: " + checkOutInfoDTO.getDeliveryTime() + ".\n";
        bodyMail += "   - Chi tiết:\n";
        newOrder.deliveryTime(checkOutInfoDTO.getDeliveryTime());
        newOrder.address(checkOutInfoDTO.getAddress());
        newOrder.customer(cart.getCustomer());
        newOrder.note(checkOutInfoDTO.getNote());
        Order savedOrder = orderRepository.save(newOrder.build());

        for(DetailOfCartDTO cartDetail : cart.getDetails()){
            OrderDetail.OrderDetailBuilder newOrderDetail = OrderDetail.builder();
            bodyMail +=  "      + " + cartDetail.getProduct() + "\n";
            newOrderDetail.order(savedOrder);
            newOrderDetail.unit(cartDetail.getUnit());
            newOrderDetail.soluong(cartDetail.getSoluong());
            newOrderDetail.product(cartDetail.getProduct());
            newOrderDetail.toppings(cartDetail.getToppings());
            orderDetailRepository.save(newOrderDetail.build());
            this.deleteCartDetail(cartDetail.getId());
        }
        customUtils.pushEmailMessageQueue("Đơn hàng của bạn tại The Coffee House",
                cart.getCustomer().getUser().getEmail(), bodyMail);
        cacheService.destroyCache("order");
        return savedOrder;
    }


    @Transactional(rollbackOn = {Exception.class, Throwable.class})
    public CartDTO addVoucher(Long id, Long voucherId){
        Customer currentCustomer = customerService.findOne(id);
        Cart cart = cartRepository.findByCustomerId(currentCustomer.getId());
        Voucher voucher = voucherRepository.findById(voucherId).orElseThrow(
                () -> new CustomException("Không tìm thấy voucher với mã là " + voucherId ,HttpStatus.NOT_FOUND)
        );
        if (voucher.getRemainingNumber() == 0){
            throw new CustomException("Voucher này đã hết!", HttpStatus.BAD_REQUEST);
        }
        cart.setVoucher(voucher);
        return cartMapper.modelTODto(cartRepository.save(cart));
    }
    public CartDTO deleteVoucher(Long id) {
        Customer currentCustomer = customerService.findOne(id);
        Cart cart = cartRepository.findByCustomerId(currentCustomer.getId());
        cart.setVoucher(null);
        cartRepository.save(cart);
        return cartMapper.modelTODto(cart);
    }

    public Long[] calculateCartTotal(CartDTO cart){
        Set<CartDetail> cartDetails = cartDetailService.findAllByCartId(cart.getId());
        Long total = 0L;
        for(CartDetail cartDetail: cartDetails){
            Long detailTotal = 0L;
            detailTotal += cartDetail.getUnit().getPrice()
                    + cartDetail.getProduct().getPrice();
            for(Topping topping: cartDetail.getToppings()){
                detailTotal += topping.getPrice();
            }
            detailTotal*= cartDetail.getSoluong();
            total += detailTotal;
        }
        Long discount = 0L;
        if (cart.getVoucher() != null){
            discount = Math.min((long)(total * cart.getVoucher().getPercentage()), cart.getVoucher().getMaxDiscount());
            total -= discount;
        }
        return new Long[]{total, discount};
    }

    public Long[] calculateCartTotal(GetFullCartDTO cart){
        Set<CartDetail> cartDetails = cartDetailService.findAllByCartId(cart.getId());
        Long total = 0L;
        Long discount = 0L;
        Voucher voucher = cart.getVoucher();
        if (voucher!= null && !isVoucherValid(null, voucher.getId())){
            Cart currentcart = cartRepository.findByCustomerId(cart.getCustomer().getId());
            currentcart.setVoucher(null);
            cartRepository.save(currentcart);
        }
        for(CartDetail cartDetail: cartDetails){
            Long detailTotal = 0L;
            detailTotal += cartDetail.getUnit().getPrice()
                    + cartDetail.getProduct().getPrice();

            for(Topping topping: cartDetail.getToppings()){
                detailTotal += topping.getPrice();
            }
            detailTotal*= cartDetail.getSoluong();
            if (voucher != null && voucher.getProducts().contains(cartDetail.getProduct())){

                discount += Math.min((long)(detailTotal * voucher.getPercentage()), voucher.getMaxDiscount());
            }
            total += detailTotal;
        }
        total -= discount;
        return new Long[]{total, discount};
    }
    public Integer calculateCartTotalAmount(GetFullCartDTO cart){
        Set<CartDetail> cartDetails = cartDetailService.findAllByCartId(cart.getId());
        Integer total = 0;
        for(CartDetail cartDetail: cartDetails){
            total += cartDetail.getSoluong();
        }
        return total;
    }

    public CartDetail insertCartDetail(AddCartDetailDTO addCartDetailDTO){
        CartDetail cartDetail = addCartDetailMapper.dtoTOModel(addCartDetailDTO);
        cartDetail.setTongtien(cartDetailService.calculateTotalDetail(cartDetail));
        return cartDetailRepository.save(cartDetail);
    }
    public GetFullCartDTO findOne(Long customerId) {
        Customer currentCustomer = customerService.findOne(customerId);
        Cart cart = cartRepository.findByCustomerId(currentCustomer.getId());
        if (cart == null){
            Customer customer = customerService.findOne(customerId);
            System.out.println(customer);
            Cart newCart = Cart.builder().customer(customer).build();
            cart = cartMapper.dtoTOModel(this.insert(newCart));
        }
        GetFullCartDTO.GetFullCartDTOBuilder getFullCartDTO = GetFullCartDTO.builder();
        getFullCartDTO.id(cart.getId());
        getFullCartDTO.note(cart.getNote());
        getFullCartDTO.createdAt(cart.getCreatedAt());
        getFullCartDTO.updatedAt(cart.getUpdatedAt());
        getFullCartDTO.customer(cart.getCustomer());
        getFullCartDTO.voucher(cart.getVoucher());
        Long[] calc = this.calculateCartTotal(getFullCartDTO.build());
        getFullCartDTO.tongtien(calc[0]);
        getFullCartDTO.discount(calc[1]);
        Set<CartDetail> details = cartDetailService.findAllByCartId(cart.getId());
        Set<DetailOfCartDTO> detailOfCartDTOS = detailOfCartMapper.modelsTODTOS(details);
        getFullCartDTO.details(detailOfCartDTOS);
        return getFullCartDTO.build();
    }
    public CartDetail  deleteCartDetail(Long id){
        return cartDetailService.delete(id);
    }
}
