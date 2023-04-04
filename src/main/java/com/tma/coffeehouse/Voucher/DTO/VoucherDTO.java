package com.tma.coffeehouse.Voucher.DTO;

import com.tma.coffeehouse.Product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoucherDTO {
    private Long id;
    private Float percentage;
    private Integer limitNumber;
    private Integer remainingNumber;
    private Date startDate;
    private String name;
    private Date endDate;
    private Long maxDiscount;
    private Long minOrderTotal;
    Set<Product> products = new HashSet<>();
    Timestamp createdAt;
    Timestamp updatedAt;

}
