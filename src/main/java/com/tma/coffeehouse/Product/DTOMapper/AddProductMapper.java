package com.tma.coffeehouse.Product.DTOMapper;

import com.tma.coffeehouse.Product.DTO.AddProductRequest;
import com.tma.coffeehouse.Product.Product;
import com.tma.coffeehouse.ProductCategory.ProductCategoryService;
import com.tma.coffeehouse.Topping.ToppingService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

@Mapper(componentModel = "spring")
public abstract class AddProductMapper {
    @Autowired
    protected ProductCategoryService productCategoryService;
    @Autowired
    @Lazy
    protected ToppingService toppingService;

    @Mappings({
            @Mapping(
                    target = "productCategory",
                    expression = "java(productCategoryService.findById(productRequest.getProductCategoryId()))"),
            @Mapping(
                    target = "productToppings",
                    expression = "java(toppingService.findManyByIDs(productRequest.getToppingsId()))")
    })
    public abstract Product dtoTOModel(AddProductRequest productRequest);
    public abstract AddProductRequest modelTODto(Product product);
}
