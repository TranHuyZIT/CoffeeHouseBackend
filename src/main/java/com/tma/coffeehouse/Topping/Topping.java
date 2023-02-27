package com.tma.coffeehouse.Topping;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tma.coffeehouse.Product.Product;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "topping")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@EqualsAndHashCode(exclude = "toppingOfProduct")
@ToString(exclude = "toppingOfProduct")
public class Topping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany(mappedBy = "productToppings")
    @JsonIgnore
    private Set<Product> toppingOfProduct = new HashSet<>();

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;


    @CreationTimestamp
    @Column(updatable = false)
    Timestamp createdAt;

    @UpdateTimestamp
    @Column
    Timestamp updatedAt;


    public void addProduct(Product product){
        this.toppingOfProduct.add(product);
    }
    public void removeProduct(Product product){
        this.toppingOfProduct.remove(product);
    }
}
