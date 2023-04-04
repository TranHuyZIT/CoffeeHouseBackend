package com.tma.coffeehouse.CartDetails.DTO;

import com.tma.coffeehouse.Product.Product;
import com.tma.coffeehouse.Topping.Topping;
import com.tma.coffeehouse.Unit.Unit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailOfCartDTO {
    private Long id;
    private Product product;
    private Unit unit;
    private Set<Topping> toppings;
    private Long tongtien = 0L;
    private Integer soluong = 1;
    private String note = "";
}
