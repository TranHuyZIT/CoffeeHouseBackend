package com.tma.coffeehouse.OrderDetails.Mapper;

import com.tma.coffeehouse.ExceptionHandling.CustomException;
import com.tma.coffeehouse.Order.Order;
import com.tma.coffeehouse.Order.OrderRepository;
import com.tma.coffeehouse.OrderDetails.DTO.AddOrderDetailDTO;
import com.tma.coffeehouse.OrderDetails.OrderDetail;
import com.tma.coffeehouse.Product.Product;
import com.tma.coffeehouse.Product.ProductRepository;
import com.tma.coffeehouse.Topping.Topping;
import com.tma.coffeehouse.Topping.ToppingRepository;
import com.tma.coffeehouse.Unit.Unit;
import com.tma.coffeehouse.Unit.UnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AddOrderDetailMapper {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UnitRepository unitRepository;
    private final ToppingRepository toppingRepository;
    public OrderDetail dtoTOModel(AddOrderDetailDTO addOrderDetailDTO){
        if ( addOrderDetailDTO == null ) {
            return null;
        }

        OrderDetail.OrderDetailBuilder orderDetail = OrderDetail.builder();
        Order order = orderRepository.findById(addOrderDetailDTO.getOrderId())
                .orElseThrow(() ->
                        new CustomException("Không tìm thấy đơn hàng với mã " + addOrderDetailDTO.getOrderId(), HttpStatus.NOT_FOUND));
        Product product = productRepository.findById(addOrderDetailDTO.getProductId())
                .orElseThrow(() -> new CustomException("Không tìm thấy sản phẩm với mã " + addOrderDetailDTO.getProductId()
                , HttpStatus.NOT_FOUND));
        Unit unit = unitRepository.findById(addOrderDetailDTO.getUnitId()).orElseThrow(() -> new CustomException("Không tìm thấy đơn vị có mã" + addOrderDetailDTO.getUnitId(), HttpStatus.NOT_FOUND));
        Long [] toppingIds = addOrderDetailDTO.getToppingIds();
        Set<Topping> topppings = new HashSet<>();
        for(Long toppingId : toppingIds){
            Topping topping = toppingRepository.findById(toppingId).orElseThrow(() -> new CustomException("Không tìm thấy topping với mã" + toppingId, HttpStatus.NOT_FOUND));
            topppings.add(topping);
        }
        orderDetail.order(order);
        orderDetail.product(product);
        orderDetail.unit(unit);
        orderDetail.toppings(topppings);
        orderDetail.soluong(addOrderDetailDTO.getSoluong() != null ? addOrderDetailDTO.getSoluong() : Integer.valueOf(1));
        return orderDetail.build();
    }
}
