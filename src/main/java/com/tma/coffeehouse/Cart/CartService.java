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
import com.tma.coffeehouse.Topping.Topping;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CartDetailService cartDetailService;
    private final CartMapper cartMapper;
    private final AddCartDetailMapper addCartDetailMapper;
    private final DetailOfCartMapper detailOfCartMapper;
    private final CustomerService customerService;
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

    @Transactional(rollbackOn = {Exception.class, Throwable.class} )
    public Order checkOutCart(Long customerId, CheckOutInfoDTO checkOutInfoDTO){
        System.out.println(checkOutInfoDTO);
        GetFullCartDTO cart = this.findOne(customerId);
        Order.OrderBuilder newOrder = Order.builder();
        newOrder.status(OrderStatus.RECEIVED);
        newOrder.tongsl(this.calculateCartTotalAmount(cart));
        newOrder.tongtien(cart.getTongtien());
        newOrder.voucher(null);
        newOrder.deliveryTime(checkOutInfoDTO.getDeliveryTime());
        newOrder.address(checkOutInfoDTO.getAddress());
        newOrder.customer(cart.getCustomer());
        newOrder.note(checkOutInfoDTO.getNote());
        Order savedOrder = orderRepository.save(newOrder.build());
        for(DetailOfCartDTO cartDetail : cart.getDetails()){
            OrderDetail.OrderDetailBuilder newOrderDetail = OrderDetail.builder();
            newOrderDetail.order(savedOrder);
            newOrderDetail.unit(cartDetail.getUnit());
            newOrderDetail.soluong(cartDetail.getSoluong());
            newOrderDetail.product(cartDetail.getProduct());
            newOrderDetail.toppings(cartDetail.getToppings());
            orderDetailRepository.save(newOrderDetail.build());
            this.deleteCartDetail(cartDetail.getId());
        }
        return savedOrder;
    }

    public Long calculateCartTotal(CartDTO cart){
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
        return total;
    }

    public Long calculateCartTotal(GetFullCartDTO cart){
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
        return total;
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
        getFullCartDTO.tongtien(this.calculateCartTotal(getFullCartDTO.build()));
        Set<CartDetail> details = cartDetailService.findAllByCartId(cart.getId());
        Set<DetailOfCartDTO> detailOfCartDTOS = detailOfCartMapper.modelsTODTOS(details);
        getFullCartDTO.details(detailOfCartDTOS);
        return getFullCartDTO.build();
    }
    public CartDetail  deleteCartDetail(Long id){
        return cartDetailService.delete(id);
    }
}
