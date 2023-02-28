package com.tma.coffeehouse.Cart.Mapper;

import com.tma.coffeehouse.Cart.Cart;
import com.tma.coffeehouse.Cart.DTO.CartDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDTO modelTODto(Cart cart);
    Cart dtoTOModel(CartDTO cartDTO);
}
