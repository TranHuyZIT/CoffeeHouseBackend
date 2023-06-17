package com.tma.coffeehouse.VoucherCode;

import com.tma.coffeehouse.Voucher.Voucher;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@Table(name = "voucher_code", indexes = @Index(name = "idx_voucher", columnList = "voucher_id"))
public class VoucherCode {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;
    @Column(nullable = false)
    private String code;
    @CreationTimestamp
    @Column(updatable = false)
    Timestamp createdAt;
}
