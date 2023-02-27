package com.tma.coffeehouse.OrderDetails;

import com.tma.coffeehouse.ExceptionHandling.CustomException;
import com.tma.coffeehouse.Order.Order;
import com.tma.coffeehouse.OrderDetails.DTO.AddOrderDetailDTO;
import com.tma.coffeehouse.OrderDetails.DTO.OrderDetailDTO;
import com.tma.coffeehouse.OrderDetails.Mapper.AddOrderDetailMapper;
import com.tma.coffeehouse.OrderDetails.Mapper.OrderDetailMapper;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderDetailMapper orderDetailMapper;
    private final AddOrderDetailMapper addOrderDetailMapper;
    public List<OrderDetailDTO> findAll(Long orderID){
        if (orderID != null){
            return orderDetailMapper.modelsTODTOs(
                    orderDetailRepository.findByOrderId(orderID)
            );
        }
        return orderDetailMapper.modelsTODTOs(
                orderDetailRepository.findAll()
        );
    }
    public OrderDetailDTO insert(AddOrderDetailDTO addOrderDetailDTO) {
        OrderDetail orderDetail = addOrderDetailMapper.dtoTOModel(addOrderDetailDTO);
        OrderDetail saved = orderDetailRepository.save(orderDetail);
        return orderDetailMapper.modelTODTO(
                saved
        );
    }
    public OrderDetailDTO update(Long id, @NotNull OrderDetail orderDetail){
        OrderDetail current = orderDetailRepository.findById(id).orElseThrow(()->new CustomException("Không tìm thấy chi tiếtdđơn hàng với mã "+ id,
                HttpStatus.NOT_FOUND));
        current.setOrder(orderDetail.getOrder());
        current.setUnit(orderDetail.getUnit());
        current.setProduct(orderDetail.getProduct());
        return orderDetailMapper.modelTODTO(
                orderDetailRepository.save(current)
        );
    }
}
