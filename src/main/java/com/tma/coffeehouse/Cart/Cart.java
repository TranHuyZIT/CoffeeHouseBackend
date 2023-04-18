package com.tma.coffeehouse.Cart;

import com.tma.coffeehouse.Customers.Customer;
import com.tma.coffeehouse.Voucher.Voucher;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cart")
@Builder

public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String note = "";
    @OneToOne(optional = false)
    private Customer customer;
    @OneToOne
    Voucher voucher;
    @CreationTimestamp
    @Column(updatable = false)
    Timestamp createdAt;
    @UpdateTimestamp
    Timestamp updatedAt;
}
