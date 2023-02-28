package com.tma.coffeehouse.Topping.Mapper;

import com.tma.coffeehouse.Topping.DTO.ToppingDTO;
import com.tma.coffeehouse.Topping.Topping;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ToppingMapper {
    Topping dtoTOModel(ToppingDTO toppingDTO);
    ToppingDTO modelTODto(Topping topping);
    List<ToppingDTO> modelsToDTOS(List<Topping> toppings);
}
