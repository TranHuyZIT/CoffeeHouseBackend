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
import com.tma.coffeehouse.ExceptionHandling.CustomException;
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

    public CartDetail insertCartDetail(AddCartDetailDTO addCartDetailDTO){
        CartDetail cartDetail = addCartDetailMapper.dtoTOModel(addCartDetailDTO);
        return cartDetailRepository.save(cartDetail);
    }
    public CartDetail deleteCartDetail(Long id){
        CartDetail cartDetail = cartDetailRepository.findById(id)
                .orElseThrow(()->new CustomException("Không tìm thấy chi tiết giỏ hàng với mã" + id, HttpStatus.NOT_FOUND));
        cartDetailRepository.delete(cartDetail);
        return cartDetail;
    }
    public GetFullCartDTO findOne(Long customerId) {
        Cart cart = cartRepository.findByCustomerId(customerId);
        GetFullCartDTO.GetFullCartDTOBuilder getFullCartDTO = GetFullCartDTO.builder();
        getFullCartDTO.note(cart.getNote());
        getFullCartDTO.createdAt(cart.getCreatedAt());
        getFullCartDTO.updatedAt(cart.getUpdatedAt());
        getFullCartDTO.customer(cart.getCustomer());
        Set<CartDetail> details = cartDetailService.findAllByCartId(cart.getId());
        Set<DetailOfCartDTO> detailOfCartDTOS = detailOfCartMapper.modelsTODTOS(details);
        getFullCartDTO.details(detailOfCartDTOS);
        return getFullCartDTO.build();
    }
}
