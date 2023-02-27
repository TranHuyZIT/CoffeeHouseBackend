package com.tma.coffeehouse.Voucher.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoucherDTO {
    private Long id;
    private Float percentage;
    private Integer limitPrice;
    private Date start;
    private Date end;
    private Integer maxDiscount;
    private Integer minOrderTotal;
    Timestamp createdAt;
    Timestamp updatedAt;

}
