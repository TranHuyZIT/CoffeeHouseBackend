package com.tma.coffeehouse.Order.DTO;

import com.tma.coffeehouse.OrderDetails.DTO.AddOrderDetailDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddOrderDTO {
    private String address;
    private Date deliveryTime;
    private String note;
    private Long voucherId = 0L;
    private Long customerId;
    private Set<AddOrderDetailDTO> details;
}
