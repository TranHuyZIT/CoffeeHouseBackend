package com.tma.coffeehouse.Voucher;

import com.tma.coffeehouse.Voucher.DTO.AddVoucherDTO;
import com.tma.coffeehouse.Voucher.DTO.VoucherDTO;
import com.tma.coffeehouse.Voucher.DTO.VoucherForCartDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class VoucherController {
    private final VoucherServiceImpl voucherServiceImpl;

    @GetMapping("/voucher")
    @ResponseStatus(HttpStatus.OK)
    public Page<Voucher> findAll(
            @RequestParam(defaultValue = "0", name="pageNo", required = false) Integer pageNo,
            @RequestParam(defaultValue = "1000", name="pageSize", required= false ) Integer pageSize,
            @RequestParam(defaultValue = "createdAt", name="sortBy") String sortBy,
            @RequestParam(defaultValue = "true", name="reverse") boolean reverse){
        return voucherServiceImpl.findAll(pageNo - 1, pageSize, sortBy, reverse);
    }
    @PostMapping("/admin/voucher")
    @ResponseStatus(HttpStatus.CREATED)
    public VoucherDTO insert(@RequestBody AddVoucherDTO addVoucherDTO){
        return voucherServiceImpl.insert(addVoucherDTO);
    }
    @PutMapping("/admin/voucher/{id}")
    @ResponseStatus(HttpStatus.OK)
    public VoucherDTO update(@PathVariable  Long id,@RequestBody AddVoucherDTO addVoucherDTO){
        return voucherServiceImpl.update(id, addVoucherDTO);
    }

    @DeleteMapping("/admin/voucher/{id}")
    @ResponseStatus(HttpStatus.OK)
    public VoucherDTO delete(@PathVariable Long id){
        return voucherServiceImpl.delete(id);
    }

    @GetMapping("/voucher/{id}")
    @ResponseStatus(HttpStatus.OK)
    public VoucherDTO getOne(@PathVariable Long id) {
        return voucherServiceImpl.findById(id);
    }
    @GetMapping("/user/voucher/cart/{id}")
    public VoucherForCartDTO getAllValidForCart(@PathVariable Long id){
        return voucherServiceImpl.findAllVoucherForCart(id);
    }
}
