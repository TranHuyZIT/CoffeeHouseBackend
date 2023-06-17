package com.tma.coffeehouse.Order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> , RevisionRepository<Order, Long, Integer> {
    Page<Order> findAll(Pageable pageable);
    Page<Order> findByCustomerId(Long customerId, Pageable pageable);
    @Query(nativeQuery = true, value = "" +
            "SELECT \n" +
            "    o.id,\n" +
            "    o.address,\n" +
            "    o.delivery_time,\n" +
            "    o.updated_at,\n" +
            "    u.name AS unit_name,\n" +
            "    p.name AS product_name,\n" +
            "    t.name AS topping_name\n" +
            "FROM\n" +
            "    order_table o\n" +
            "        JOIN\n" +
            "    order_detail od ON (o.id = od.order_id)\n" +
            "        JOIN\n" +
            "    product p ON (p.id = od.product_id)\n" +
            "        JOIN\n" +
            "    unit u ON (u.id = od.unit_id)\n" +
            "        JOIN\n" +
            "    order_detail_topping odt ON (odt.od_id = od.id)\n" +
            "        JOIN\n" +
            "    topping t ON (t.id = odt.topping_id)\n" +
            "WHERE\n" +
            "    o.created_at BETWEEN DATE(:startDate) AND DATE(:endDate);")
    List<Object> getOrderForReport(@Param("startDate") Date startDate,@Param("endDate") Date endDate);

}
