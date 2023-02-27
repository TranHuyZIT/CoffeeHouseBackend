package com.tma.coffeehouse.Product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tma.coffeehouse.ProductCategory.ProductCategory;
import com.tma.coffeehouse.Topping.Topping;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@EqualsAndHashCode(exclude = "productToppings")
@ToString(exclude = "productToppings")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany(
            fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "product_topping",
            joinColumns = @JoinColumn(name = "productID"),
            inverseJoinColumns = @JoinColumn(name = "toppingID")
    )
    @JsonIgnore
    private Set<Topping> productToppings = new HashSet<>();
    @Column(nullable = false)
    private String name;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private ProductCategory productCategory;

    @Column(nullable = false)
    private Integer price;
    @Column(nullable = false)
    private String image;
    @Column(nullable = false)
    private String description;

    @CreationTimestamp
    @Column(updatable = false)
    Timestamp createdAt;
    @UpdateTimestamp
    Timestamp updatedAt;

    public void addTopping(Topping topping){
        this.productToppings.add(topping);
    }
}
