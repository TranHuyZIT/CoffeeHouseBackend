package com.tma.coffeehouse.Order.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tma.coffeehouse.Customers.Customer;
import com.tma.coffeehouse.Order.OrderStatus;
import com.tma.coffeehouse.Voucher.Voucher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@RequiredArgsConstructor
@Builder
public class OrderDTO {
    private long id;
    private String address;
    private Date deliveryTime;
    private String note;
    private Voucher voucher;
    private Customer customer;
    private OrderStatus status = OrderStatus.RECEIVED;

}
