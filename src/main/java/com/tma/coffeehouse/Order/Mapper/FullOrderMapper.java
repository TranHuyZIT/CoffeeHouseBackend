package com.tma.coffeehouse.Order.Mapper;

import com.tma.coffeehouse.Order.DTO.DetailOfOrder;
import com.tma.coffeehouse.Order.DTO.FullOrderDTO;
import com.tma.coffeehouse.Order.Order;
import com.tma.coffeehouse.OrderDetails.DTO.OrderDetailDTO;
import com.tma.coffeehouse.OrderDetails.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class FullOrderMapper {
    private final OrderDetailService orderDetailService;
    public FullOrderDTO modelTODto(Order order){
        if ( order == null ) {
            return null;
        }

        FullOrderDTO.FullOrderDTOBuilder fullOrderDTO = FullOrderDTO.builder();

        if ( order.getId() != null ) {
            fullOrderDTO.id( order.getId() );
        }
        fullOrderDTO.address( order.getAddress() );
        fullOrderDTO.deliveryTime( order.getDeliveryTime() );
        fullOrderDTO.note( order.getNote() );
        fullOrderDTO.voucher( order.getVoucher() );
        fullOrderDTO.customer( order.getCustomer() );
        fullOrderDTO.status( order.getStatus() );

        Set<OrderDetailDTO> details = new HashSet<>(orderDetailService.findAll(order.getId()));
        Set<DetailOfOrder> detailOfOrders = new HashSet<>();
        for(OrderDetailDTO detailDTO: details){
            DetailOfOrder detailOfOrder = DetailOfOrder.builder()
                    .unit(detailDTO.getUnit())
                    .product(detailDTO.getProduct())
                    .toppings(detailDTO.getToppings())
                    .id(detailDTO.getId())
                    .build();
            detailOfOrders.add(detailOfOrder);
        }
        fullOrderDTO.details(detailOfOrders);

        return fullOrderDTO.build();
    }
}
