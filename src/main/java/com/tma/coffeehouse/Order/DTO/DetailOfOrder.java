package com.tma.coffeehouse.Order.DTO;

import com.tma.coffeehouse.Order.Order;
import com.tma.coffeehouse.Product.Product;
import com.tma.coffeehouse.Topping.Topping;
import com.tma.coffeehouse.Unit.Unit;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class DetailOfOrder {
    private Long id;
    private Product product;
    private Set<Topping> toppings;

    private Unit unit;
}
