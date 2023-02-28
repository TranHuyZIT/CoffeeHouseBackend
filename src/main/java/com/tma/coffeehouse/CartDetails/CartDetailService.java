package com.tma.coffeehouse.CartDetails;

import lombok.RequiredArgsConstructor;
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
}
