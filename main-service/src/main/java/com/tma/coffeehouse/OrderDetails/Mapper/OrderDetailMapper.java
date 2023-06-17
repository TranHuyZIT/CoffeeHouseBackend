package com.tma.coffeehouse.OrderDetails.Mapper;

import com.tma.coffeehouse.Order.DTO.OrderDTO;
import com.tma.coffeehouse.Order.Order;
import com.tma.coffeehouse.OrderDetails.DTO.OrderDetailDTO;
import com.tma.coffeehouse.OrderDetails.OrderDetail;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    OrderDetail dtoTOModel (OrderDTO orderDTO);
    OrderDetailDTO modelTODTO (OrderDetail order);
    List <OrderDetailDTO> modelsTODTOs(List<OrderDetail> orderDetails);
}
