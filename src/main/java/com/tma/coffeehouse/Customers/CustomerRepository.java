package com.tma.coffeehouse.Customers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Page<Customer> findAll(Pageable pageable);
    Page<Customer> findByUser_NameContaining(String name, Pageable pageable);
    Optional<Customer> findByUser_Id(Long userId);
}
