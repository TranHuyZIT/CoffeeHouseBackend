package com.tma.coffeehouse.Order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> , RevisionRepository<Order, Long, Integer> {
    Page<Order> findAll(Pageable pageable);
    Page<Order> findByCustomerId(Long customerId, Pageable pageable);
}
