package com.tma.coffeehouse.Voucher;

import com.tma.coffeehouse.Voucher.DTO.VoucherDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
//    Page<Voucher> findAllAvailable(Pageable pageable);
    Page<Voucher> findAll(Pageable pageable);
}
