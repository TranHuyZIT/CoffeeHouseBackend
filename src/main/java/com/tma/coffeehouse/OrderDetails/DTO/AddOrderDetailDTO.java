package com.tma.coffeehouse.OrderDetails.DTO;

import com.tma.coffeehouse.Order.Order;
import com.tma.coffeehouse.Product.Product;
import com.tma.coffeehouse.Topping.Topping;
import com.tma.coffeehouse.Unit.Unit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddOrderDetailDTO {
    private Long productId;
    private Long unitId;
    private Long[] toppingIds;
    private Long orderId;
}
