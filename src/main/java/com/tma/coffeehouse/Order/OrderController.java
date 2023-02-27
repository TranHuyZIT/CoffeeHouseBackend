package com.tma.coffeehouse.Order;

import com.tma.coffeehouse.Order.DTO.AddOrderDTO;
import com.tma.coffeehouse.Order.DTO.FullOrderDTO;
import com.tma.coffeehouse.Order.DTO.OrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @GetMapping("/{id}")
    public FullOrderDTO findOne (@PathVariable Long id){
        return orderService.findById(id);
    }
    @PostMapping
    public FullOrderDTO insert(@RequestBody AddOrderDTO addOrderDTO){
        return  orderService.insert(addOrderDTO);
    }
}
