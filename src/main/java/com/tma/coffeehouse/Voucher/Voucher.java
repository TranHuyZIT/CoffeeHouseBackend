package com.tma.coffeehouse.Voucher;

import com.tma.coffeehouse.Product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "voucher")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Float percentage;
    @Column (nullable = false)
    private Integer limitPrice;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)

    private Date start;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)

    private Date end;
    @Column (nullable = false)
    private Integer maxDiscount;
    @Column (nullable = false)
    private Integer minOrderTotal;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            joinColumns = @JoinColumn(name = "voucherId"),
            inverseJoinColumns = @JoinColumn(name = "productId")
    )
    Set<Product> products = new HashSet<>();

    @CreationTimestamp
    @Column(updatable = false)
    Timestamp createdAt;
    @UpdateTimestamp
    Timestamp updatedAt;
}
