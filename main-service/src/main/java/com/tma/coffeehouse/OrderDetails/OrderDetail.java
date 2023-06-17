package com.tma.coffeehouse.OrderDetails;

import com.tma.coffeehouse.Order.Order;
import com.tma.coffeehouse.Product.Product;
import com.tma.coffeehouse.Topping.Topping;
import com.tma.coffeehouse.Unit.Unit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_detail")
public class OrderDetail {
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

    @Column
    private Integer soluong = 1;
    @ManyToMany
    @JoinTable(
            name = "orderDetail_topping",
            joinColumns = @JoinColumn(name = "odId"),
            inverseJoinColumns = @JoinColumn(name = "toppingId")
    )
    private Set<Topping> toppings;



}
