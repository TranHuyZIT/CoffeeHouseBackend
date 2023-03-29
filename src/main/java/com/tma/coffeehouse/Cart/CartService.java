package com.tma.coffeehouse.Cart;

import com.tma.coffeehouse.Cart.DTO.CartDTO;
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
import com.tma.coffeehouse.Topping.Topping;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
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

    public CartDetail insertCartDetail(AddCartDetailDTO addCartDetailDTO){
        CartDetail cartDetail = addCartDetailMapper.dtoTOModel(addCartDetailDTO);
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
        Set<CartDetail> details = cartDetailService.findAllByCartId(cart.getId());
        Set<DetailOfCartDTO> detailOfCartDTOS = detailOfCartMapper.modelsTODTOS(details);
        getFullCartDTO.details(detailOfCartDTOS);
        return getFullCartDTO.build();
    }
    public CartDetail  deleteCartDetail(Long id){
        return cartDetailService.delete(id);
    }
}
