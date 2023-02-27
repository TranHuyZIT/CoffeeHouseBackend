package com.tma.coffeehouse.Voucher;

import com.tma.coffeehouse.ExceptionHandling.CustomException;
import com.tma.coffeehouse.Voucher.DTO.VoucherDTO;
import com.tma.coffeehouse.Voucher.Mapper.VoucherMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoucherService {
    private final VoucherRepository voucherRepository;
    private final VoucherMapper voucherMapper;
    Page<VoucherDTO> findAll(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        Page<Voucher> page = voucherRepository.findAll(pageable);
        List<VoucherDTO> voucherDTO = voucherMapper.modelsTODTOS(
                page.getContent()
        );
        Page<VoucherDTO> pageDTO = page.map(voucherMapper::modelTODto);
        return pageDTO;
    }
    VoucherDTO findById(Long id){
        return voucherMapper.modelTODto(
            voucherRepository.findById(id).orElseThrow(()-> new CustomException("Không tìm thấy voucher có id là " + id, HttpStatus.NOT_FOUND))
        );
    }
    Page<VoucherDTO> findAllAvailable(){
        return null;
    }
    public VoucherDTO insert(Voucher voucher){
        Voucher saved = voucherRepository.save(voucher);
        return voucherMapper.modelTODto(saved);
    }
    public VoucherDTO update(long id, Voucher newVoucher){
        Voucher current = voucherRepository.findById(id)
                .orElseThrow(()-> new CustomException("Không tìm thấy voucher có id là " + id, HttpStatus.NOT_FOUND));
        current.setEnd(newVoucher.getEnd());
        current.setLimitPrice(newVoucher.getLimitPrice());
        current.setStart(newVoucher.getStart());
        current.setPercentage(newVoucher.getPercentage());
        current.setMaxDiscount(newVoucher.getMaxDiscount());
        current.setMinOrderTotal(newVoucher.getMinOrderTotal());
        Voucher saved = voucherRepository.save(current);
        return voucherMapper.modelTODto(saved);
    }
    public VoucherDTO delete(Long id){
        Voucher current = voucherRepository.findById(id)
                .orElseThrow(()-> new CustomException("Không tìm thấy voucher có id là " + id, HttpStatus.NOT_FOUND));
        voucherRepository.delete(current);
        return voucherMapper.modelTODto(current);
    }
}
