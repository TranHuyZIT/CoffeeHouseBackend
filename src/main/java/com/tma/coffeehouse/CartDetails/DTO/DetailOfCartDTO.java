package com.tma.coffeehouse.CartDetails.DTO;

import com.tma.coffeehouse.Product.Product;
import com.tma.coffeehouse.Topping.Topping;
import com.tma.coffeehouse.Unit.Unit;

import java.util.Set;

public class DetailOfCartDTO {
    private Long id;
    private Product product;
    private Unit unit;
    private Set<Topping> toppings;
}
