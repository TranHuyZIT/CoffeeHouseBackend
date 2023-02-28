package com.tma.coffeehouse.Order;

import com.tma.coffeehouse.ExceptionHandling.CustomException;
import com.tma.coffeehouse.Order.DTO.AddOrderDTO;
import com.tma.coffeehouse.Order.DTO.FullOrderDTO;
import com.tma.coffeehouse.Order.DTO.OrderDTO;
import com.tma.coffeehouse.Order.Mapper.AddOrderMapper;
import com.tma.coffeehouse.Order.Mapper.FullOrderMapper;
import com.tma.coffeehouse.Order.Mapper.OrderMapper;
import com.tma.coffeehouse.OrderDetails.DTO.AddOrderDetailDTO;
import com.tma.coffeehouse.OrderDetails.DTO.OrderDetailDTO;
import com.tma.coffeehouse.OrderDetails.OrderDetail;
import com.tma.coffeehouse.OrderDetails.OrderDetailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderDetailService orderDetailService;
    private final OrderRepository orderRepository;
    private final AddOrderMapper addOrderMapper;
    private final FullOrderMapper fullOrderMapper;
    private final OrderMapper orderMapper;

    @Transactional(rollbackOn = {CustomException.class, Exception.class, Throwable.class})
    public FullOrderDTO insert(AddOrderDTO addOrderDTO){
        Order order = addOrderMapper.dtoTOModel(addOrderDTO);
        Order saved = orderRepository.save(order);
        Set<AddOrderDetailDTO> details = addOrderDTO.getDetails();
        for(AddOrderDetailDTO detail: details){
            detail.setOrderId(saved.getId());
            OrderDetailDTO orderDetailDTO= orderDetailService.insert(detail);
        }
        FullOrderDTO fullOrderDTO = fullOrderMapper.modelTODto(order);
        return fullOrderDTO;
    }
    public FullOrderDTO findById(Long id){
        Order order = orderRepository.findById(id)
                .orElseThrow(()->new CustomException("Không tìm thấy đơn hàng với mã" + id, HttpStatus.NOT_FOUND));
        FullOrderDTO fullOrderDTO = fullOrderMapper.modelTODto(order);
        return fullOrderDTO;
    }
}
