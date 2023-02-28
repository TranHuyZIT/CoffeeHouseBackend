package com.tma.coffeehouse.Product.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddProductRequest {
    private String name;
    private Integer price;
    private String description;
    private Long productCategoryId;
    private Long[] toppingsId;
}
