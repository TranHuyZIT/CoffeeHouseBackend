package com.tma.coffeehouse.Order.Mapper;

import com.tma.coffeehouse.Order.DTO.OrderDTO;
import com.tma.coffeehouse.Order.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order dtoTOModel(OrderDTO orderDTO);
    OrderDTO modelTODTO (Order order);
    List<OrderDTO> modelsTODTOS(List<Order> orders);
}
