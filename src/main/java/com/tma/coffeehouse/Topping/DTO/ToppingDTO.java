package com.tma.coffeehouse.Topping.DTO;

import com.tma.coffeehouse.Product.Product;
import lombok.*;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ToppingDTO {
    private Long id;
    private Set<Product> toppingOfProduct = new HashSet<>();
    private String name;
    private Integer price;
    Timestamp createdAt;
    Timestamp updatedAt;
}
