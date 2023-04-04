package com.tma.coffeehouse.Voucher;

import com.tma.coffeehouse.Voucher.DTO.AddVoucherDTO;
import com.tma.coffeehouse.Voucher.DTO.VoucherDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Set;

public interface VoucherService {
    Page<Voucher> findAll(Integer pageNo, Integer pageSize, String sortBy, Boolean reverse);
    VoucherDTO findById(Long id);
     VoucherDTO insert(AddVoucherDTO addVoucherDTO);
     VoucherDTO update(long id, AddVoucherDTO addVoucherDTO);
     Set<VoucherDTO> findAllVoucherForCart(Long customerId);
     VoucherDTO delete(Long id);
}
