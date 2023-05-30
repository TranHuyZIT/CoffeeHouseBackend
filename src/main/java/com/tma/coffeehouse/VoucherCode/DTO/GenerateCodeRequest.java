package com.tma.coffeehouse.VoucherCode.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class GenerateCodeRequest {
    Long voucherId;
    int number;
}
