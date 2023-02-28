package com.tma.coffeehouse.Cart.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    private Long id;
    private String note = "";
    Timestamp createdAt;
    Timestamp updatedAt;

}
