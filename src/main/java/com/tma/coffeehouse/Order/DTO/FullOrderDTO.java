package com.tma.coffeehouse.Order.DTO;

import com.tma.coffeehouse.Customers.Customer;
import com.tma.coffeehouse.Order.OrderStatus;
import com.tma.coffeehouse.OrderDetails.DTO.AddOrderDetailDTO;
import com.tma.coffeehouse.OrderDetails.DTO.OrderDetailDTO;
import com.tma.coffeehouse.OrderDetails.OrderDetail;
import com.tma.coffeehouse.Voucher.Voucher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FullOrderDTO {
    private long id;
    private String address;
    private Date deliveryTime;
    private String note;
    private Voucher voucher;
    private Customer customer;
    private OrderStatus status = OrderStatus.RECEIVED;
    private Set<DetailOfOrder> details;
    private Long tongtien = 0L;
    private Integer tongsl = 0;
    Timestamp createdAt;
    Timestamp updatedAt;
}
