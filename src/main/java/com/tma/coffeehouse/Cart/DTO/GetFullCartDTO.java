package com.tma.coffeehouse.Cart.DTO;

import com.tma.coffeehouse.CartDetails.DTO.DetailOfCartDTO;
import com.tma.coffeehouse.Customers.Customer;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
public class GetFullCartDTO {
    private Long id;
    private String note = "";
    private Customer customer;
    private Long tongtien ;
    Timestamp createdAt;
    Timestamp updatedAt;
    Set<DetailOfCartDTO> details = new HashSet<>();
}
