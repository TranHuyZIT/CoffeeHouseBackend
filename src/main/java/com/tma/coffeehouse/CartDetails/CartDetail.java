package com.tma.coffeehouse.CartDetails;

import com.tma.coffeehouse.Order.Order;
import com.tma.coffeehouse.Product.Product;
import com.tma.coffeehouse.Topping.Topping;
import com.tma.coffeehouse.Unit.Unit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cart_detail")
public class CartDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;

    @ManyToMany
    @JoinTable(
            name = "cartDetail_topping",
            joinColumns = @JoinColumn(name = "cdId"),
            inverseJoinColumns = @JoinColumn(name = "toppingId")
    )
    private Set<Topping> toppings;


}
