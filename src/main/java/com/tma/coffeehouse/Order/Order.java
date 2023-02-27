package com.tma.coffeehouse.Order;

import com.tma.coffeehouse.Customers.Customer;
import com.tma.coffeehouse.Voucher.Voucher;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.Date;

@Entity
@Table(name = "order_table")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveryTime;
    @Column(nullable = false)
    private String note;
    @OneToOne(optional = true)
    private Voucher voucher;
    @Column
    private OrderStatus status = OrderStatus.RECEIVED;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
