package com.tma.coffeehouse.CartDetails.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddCartDetailDTO {
    private Long productId;
    private Long unitId;
    private Long customerId;
    private Long[] toppingIds;
    private String note = "";
    private Integer soluong;
}
