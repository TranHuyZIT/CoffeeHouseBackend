package com.tma.coffeehouse.Order;

import com.tma.coffeehouse.Customers.Customer;
import com.tma.coffeehouse.Voucher.Voucher;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "order_table", indexes = @Index(name = "idx_customer", columnList = "customer_id"))
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Audited
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String address;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveryTime;
    @Column
    private String note;
    @OneToOne
    @Audited
    private Voucher voucher;
    @Column
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.RECEIVED;

    @Column
    private Long tongtien = 0L;
    @Column
    private Integer tongsl = 0;
    @ManyToOne
    @Audited
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @CreationTimestamp
    @Column(updatable = false)
    Timestamp createdAt;
    @UpdateTimestamp
    Timestamp updatedAt;
}
