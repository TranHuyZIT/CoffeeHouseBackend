package com.tma.coffeehouse.Voucher.Mapper;

import com.tma.coffeehouse.Voucher.DTO.VoucherDTO;
import com.tma.coffeehouse.Voucher.Voucher;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VoucherMapper {
    Voucher dtoTOModel (VoucherDTO voucherDTO);
    VoucherDTO modelTODto(Voucher voucher);
    List<VoucherDTO> modelsTODTOS(List<Voucher> vouchers);
}
