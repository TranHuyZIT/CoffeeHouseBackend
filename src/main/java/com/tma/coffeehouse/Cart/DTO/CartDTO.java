package com.tma.coffeehouse.Cart.DTO;

import com.tma.coffeehouse.Customers.Customer;
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
    private Customer customer;
    Timestamp createdAt;
    Timestamp updatedAt;

}
