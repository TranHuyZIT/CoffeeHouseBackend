package com.tma.coffeehouse.CartDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tma.coffeehouse.Cart.Cart;
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
    @JoinColumn(name = "cart_id")
    @JsonIgnore
    private Cart cart;
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

    @Column
    private String note = "";
    @Column(nullable = false)
    private Integer soluong;
}
