package com.tma.coffeehouse.Voucher.DTO;


import com.tma.coffeehouse.Product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddVoucherDTO {
    private Float percentage;
    private Integer limitNumber;
    private String name;
    private Date endDate;
    private Date startDate;
    private Long maxDiscount;
    private Long minOrderTotal;
    private Long[] productsId ;
}
