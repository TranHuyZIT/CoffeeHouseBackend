package com.tma.coffeehouse.Cart.DTO;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckOutInfoDTO {
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveryTime;
    private String address;
    private String note;
    private Long voucherId = null;
}
