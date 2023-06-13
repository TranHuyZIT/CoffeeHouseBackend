package com.tma.coffeehouse.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value="SELECT p FROM Product AS p JOIN ProductCategory AS pc ON p.productCategory = pc  WHERE (:prodCategoryID = 0 or pc.id = :prodCategoryID) AND (:name = '' or LOWER(p.name) like CONCAT('%',:name,'%'))")
    Page<Product> findAllByQueries(@Param("prodCategoryID") Long prodCategoryID, @Param("name") String name, Pageable pageable);

    @Query(value = "SELECT p FROM Product p WHERE p.image IN :images")
    List<Product> findAllByImages(@Param("images")String[] image);
}
