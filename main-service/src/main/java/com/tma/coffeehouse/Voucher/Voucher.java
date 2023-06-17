package com.tma.coffeehouse.Voucher;

import com.tma.coffeehouse.Product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

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
@Audited
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Float percentage;
    @Column (nullable = false)
    private Integer limitNumber;
    @Column(nullable = false)
    private Integer remainingNumber;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)

    private Date endDate;
    @Column (nullable = false)
    private Long maxDiscount;
    @Column (nullable = false)
    private Long minOrderTotal;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            joinColumns = @JoinColumn(name = "voucherId"),
            inverseJoinColumns = @JoinColumn(name = "productId")
    )
    @NotAudited
    Set<Product> products = new HashSet<>();
    @CreationTimestamp
    @Column(updatable = false)
    Timestamp createdAt;
    @UpdateTimestamp
    Timestamp updatedAt;
}
