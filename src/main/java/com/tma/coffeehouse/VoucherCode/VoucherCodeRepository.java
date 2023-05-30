package com.tma.coffeehouse.VoucherCode;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VoucherCodeRepository extends JpaRepository<VoucherCode, Long> {
    List<VoucherCode> findAllByVoucherId(Long voucherId);
    VoucherCode findOneByVoucherId(Long voucherId);
}
