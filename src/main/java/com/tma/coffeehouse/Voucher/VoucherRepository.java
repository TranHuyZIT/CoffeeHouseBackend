package com.tma.coffeehouse.Voucher;

import com.tma.coffeehouse.Voucher.DTO.VoucherDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Set;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    Page<Voucher> findAll(Pageable pageable);

    @Query("SELECT v FROM Voucher v WHERE (?1 BETWEEN v.startDate AND v.endDate) AND (v.limitNumber > 0)")
    Set<Voucher> findAllAvailable(Date currentDate);
}
