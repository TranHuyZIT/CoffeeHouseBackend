package com.tma.coffeehouse.Voucher;

import com.tma.coffeehouse.Voucher.DTO.AddVoucherDTO;
import com.tma.coffeehouse.Voucher.DTO.VoucherDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/voucher")
@RequiredArgsConstructor
public class VoucherController {
    private final VoucherServiceImpl voucherServiceImpl;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<VoucherDTO> findAll(
            @RequestParam(defaultValue = "0", name="pageNo") Integer pageNo,
            @RequestParam(defaultValue = "10", name="pageSize") Integer pageSize,
            @RequestParam(defaultValue = "createdAt", name="sortBy") String sortBy){
        return voucherServiceImpl.findAll(pageNo, pageSize, sortBy);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VoucherDTO insert(@RequestBody AddVoucherDTO addVoucherDTO){
        return voucherServiceImpl.insert(addVoucherDTO);
    }
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public VoucherDTO update(@PathVariable  Long id, AddVoucherDTO addVoucherDTO){
        return voucherServiceImpl.update(id, addVoucherDTO);
    }

}
