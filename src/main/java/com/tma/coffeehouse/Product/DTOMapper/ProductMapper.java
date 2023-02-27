package com.tma.coffeehouse.Product.DTOMapper;

import com.tma.coffeehouse.Product.DTO.ProductDTO;
import com.tma.coffeehouse.Product.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO modelTODto(Product product);
    Product dtoTOModel(ProductDTO productDTO);
}
