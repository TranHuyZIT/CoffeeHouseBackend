package com.tma.coffeehouse.Topping;

import com.tma.coffeehouse.Product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToppingRepository  extends JpaRepository<Topping, Long> {
    List<Topping> findByToppingOfProduct_Id(Long productId);
    Page<Topping> findAll(Pageable pageable);
    Page<Topping> findByNameContaining(String name, Pageable pageable);
    Page<Topping> findByToppingOfProduct_Id(Long productID, Pageable pageable);
}
