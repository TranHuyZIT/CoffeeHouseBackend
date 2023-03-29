package com.tma.coffeehouse.CartDetails;

import com.tma.coffeehouse.ExceptionHandling.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CartDetailService {
    private final CartDetailRepository cartDetailRepository;
    public Set<CartDetail> findAllByCartId(Long cartId){
        return new HashSet<>(cartDetailRepository.findByCartId(cartId));
    }
    public CartDetail delete(Long id){
        CartDetail cartDetail = cartDetailRepository.findById(id).orElseThrow(
                () -> new CustomException("Không tìm thấy chi tiết đơn hàng với mã " + id, HttpStatus.NOT_FOUND));
        cartDetailRepository.delete(cartDetail);
        return  cartDetail;
    }
}
