package com.tma.coffeehouse.Topping.DTO;

import com.tma.coffeehouse.Product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddToppingDTO {
    private Long[] productIds = new Long[0];
    private String name;
    private Integer price;
}
