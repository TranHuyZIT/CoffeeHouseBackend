package com.tma.coffeehouse.OrderDetails;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    public List<OrderDetail> findByOrderId(Long orderID);
}
