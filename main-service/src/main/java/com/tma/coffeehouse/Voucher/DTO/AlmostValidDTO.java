package com.tma.coffeehouse.Voucher.DTO;

import com.tma.coffeehouse.Product.Product;
import com.tma.coffeehouse.Voucher.Voucher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlmostValidDTO {
    VoucherDTO voucher;
    Set<Product> products;
}
