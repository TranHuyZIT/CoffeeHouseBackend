package com.tma.coffeehouse.Cart.DTO;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CheckOutInfoDTO {
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveryTime;
    private String address;
    private String note;
    private Long voucherId = null;
}
