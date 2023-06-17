package com.tma.coffeehouse.Voucher.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoucherForCartDTO {
    Set<VoucherDTO> valid = new HashSet<>();
    Set<AlmostValidDTO> almostValid = new HashSet<>();
}
