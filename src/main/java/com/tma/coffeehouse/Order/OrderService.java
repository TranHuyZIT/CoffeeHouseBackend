package com.tma.coffeehouse.Order;

import com.tma.coffeehouse.Cart.DTO.CartDTO;
import com.tma.coffeehouse.CartDetails.CartDetail;
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
import com.tma.coffeehouse.Topping.Topping;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
    public FullOrderDTO insert(){
        Order order = Order.builder()
                .address("").note("").voucher(null).customer(null)
                .deliveryTime(new Date()).status(OrderStatus.RECEIVED)
                .tongsl(0)
                .tongtien(0L)
                .build();

        Order saved = orderRepository.save(order);
        FullOrderDTO fullOrderDTO = fullOrderMapper.modelTODto(order);
        return fullOrderDTO;
    }
    @Transactional(rollbackOn = {CustomException.class, Exception.class, Throwable.class})
    public FullOrderDTO update(Long id, AddOrderDTO addOrderDTO){
        Order currentOrder = orderRepository.findById(id)
                .orElseThrow(()->new CustomException("Không tìm thấy đơn hàng với mã" + id, HttpStatus.NOT_FOUND));
        Set<OrderDetailDTO> currentDetails = new HashSet<>(orderDetailService.findAll(id));
        for(OrderDetailDTO orderDetail: currentDetails){
            orderDetailService.delete(orderDetail.getId());
        }
        Order newOrder = addOrderMapper.dtoTOModel(addOrderDTO);
        Set<AddOrderDetailDTO> details = addOrderDTO.getDetails();
        for(AddOrderDetailDTO detail: details){
            detail.setOrderId(id);
            OrderDetailDTO orderDetailDTO= orderDetailService.insert(detail);
        }
        currentOrder.setTongtien(calculateOrderTotal(id));
        currentOrder.setTongsl(details.size());
        currentOrder.setAddress(newOrder.getAddress());
        currentOrder.setNote(newOrder.getNote());
        currentOrder.setCustomer(newOrder.getCustomer());
        currentOrder.setDeliveryTime(newOrder.getDeliveryTime());
        currentOrder.setVoucher(newOrder.getVoucher());
        currentOrder.setStatus(newOrder.getStatus());
        FullOrderDTO fullOrderDTO = fullOrderMapper.modelTODto(currentOrder);
        orderRepository.save(currentOrder);
        return fullOrderDTO;
    }
    public Long calculateOrderTotal(Long orderId){
        Set<OrderDetailDTO> details = new HashSet<>(orderDetailService.findAll(orderId));
        Long total = 0L;
        for(OrderDetailDTO detail: details){
            Long detailTotal = 0L;
            detailTotal += detail.getUnit().getPrice()
                    + detail.getProduct().getPrice();
            for(Topping topping: detail.getToppings()){
                detailTotal += topping.getPrice();
            }
            detailTotal*= detail.getSoluong();
            total += detailTotal;
        }
        return total;
    }
    public FullOrderDTO findById(Long id){
        Order order = orderRepository.findById(id)
                .orElseThrow(()->new CustomException("Không tìm thấy đơn hàng với mã" + id, HttpStatus.NOT_FOUND));
        return fullOrderMapper.modelTODto(order);
    }
    public FullOrderDTO deleteById(Long id){
        Order order = orderRepository.findById(id)
                .orElseThrow(()->new CustomException("Không tìm thấy đơn hàng với mã" + id, HttpStatus.NOT_FOUND));
        List<OrderDetailDTO> detailDTOS = orderDetailService.findAll(id);
        for(OrderDetailDTO detail: detailDTOS){
            orderDetailService.delete(detail.getId());
        }
        orderRepository.delete(order);
        return fullOrderMapper.modelTODto(order);
    }
    public Page<Order> findAll(Long customerId, Integer pageNo, Integer pageSize, String sortBy, boolean reverse){
        System.out.println(customerId);
        if (pageNo == -1){
            Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(reverse? Sort.Direction.DESC : Sort.Direction.ASC, sortBy));
            return orderRepository.findAll(pageable);
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(reverse? Sort.Direction.DESC : Sort.Direction.ASC, sortBy));
        if (customerId != null){

            return orderRepository.findByCustomer_Id(customerId, pageable);
        }
        return orderRepository.findAll(pageable);
    }
}
