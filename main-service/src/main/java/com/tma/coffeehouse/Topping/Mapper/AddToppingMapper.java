package com.tma.coffeehouse.Topping.Mapper;

import com.tma.coffeehouse.Product.ProductService;
import com.tma.coffeehouse.Topping.DTO.AddToppingDTO;
import com.tma.coffeehouse.Topping.Topping;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class AddToppingMapper {
    @Autowired
    protected ProductService productService;
    @Mappings(
            {
                    @Mapping(
                            target = "toppingOfProduct",
                            expression = "java(productService.findManyByIDs(addToppingDTO.getProductIds()))"
                    )
            }
    )
    public abstract Topping dtoTOModel (AddToppingDTO addToppingDTO);

}
