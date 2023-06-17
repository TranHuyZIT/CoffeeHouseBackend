package com.tma.coffeehouse.Product.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tma.coffeehouse.ProductCategory.ProductCategory;
import com.tma.coffeehouse.Topping.Topping;
import lombok.*;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@RequiredArgsConstructor
@Builder
public class ProductDTO {
    private Long id;
    private String name;
    private ProductCategory productCategory;
    private Integer price;
    private String image;
    private String description;
    private Set<Topping> productToppings = new HashSet<>();
    Timestamp createdAt;
    Timestamp updatedAt;

}
