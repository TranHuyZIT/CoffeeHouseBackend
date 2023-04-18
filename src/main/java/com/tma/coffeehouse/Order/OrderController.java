package com.tma.coffeehouse.Order;

import com.tma.coffeehouse.Order.DTO.AddOrderDTO;
import com.tma.coffeehouse.Order.DTO.FullOrderDTO;
import com.tma.coffeehouse.Order.DTO.OrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @GetMapping("user/order/{id}")
    public FullOrderDTO findOne (@PathVariable Long id){
        return orderService.findById(id);
    }
    @PutMapping("admin/order/{id}")
    public FullOrderDTO update(@PathVariable Long id, @RequestBody AddOrderDTO addOrderDTO) {
        return orderService.update(id, addOrderDTO);
    }
    @DeleteMapping("admin/order/{id}")
    public FullOrderDTO delete(@PathVariable Long id){
        return orderService.deleteById(id);
    }
    @PostMapping("admin/order")
    public FullOrderDTO insert(){
        return  orderService.insert();
    }
    @GetMapping("user/order")
    @ResponseStatus(HttpStatus.OK)
    public Page<Order> findAll(
            @RequestParam (defaultValue = "", name = "customerId", required = false) Long customerId,
            @RequestParam(defaultValue = "0", name="pageNo", required = false) Integer pageNo,
            @RequestParam(defaultValue = "10", name="pageSize", required = false) Integer pageSize,
            @RequestParam(defaultValue = "createdAt", name="sortBy", required = false) String sortBy,
            @RequestParam(defaultValue = "true", name="reverse", required = false) boolean reverse
    ){
        return orderService.findAll(customerId , pageNo - 1, pageSize, sortBy, reverse);
    }
}
