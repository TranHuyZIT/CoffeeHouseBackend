package com.tma.coffeehouse.VoucherCode;

import com.tma.coffeehouse.VoucherCode.DTO.GenerateCodeRequest;
import com.tma.coffeehouse.config.JsonArg;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class VoucherCodeController {
    private final VoucherCodeService voucherCodeService;

    @GetMapping("admin/code")
    public List<VoucherCode> getAll(@RequestParam(name="voucherId", defaultValue = "") Long voucherId){
        return voucherCodeService.getAll(voucherId);
    }
    @PostMapping("admin/code/{id}")
        public VoucherCode sendCodeFromVoucher(@PathVariable Long id, @JsonArg(value="userId") Long userId){
        return voucherCodeService.sendOneToUser(id, userId);
    }
    @PostMapping("admin/code/generate")
    public List<VoucherCode> generate(@RequestBody GenerateCodeRequest generateCodeRequest){
        return voucherCodeService.generateVoucherCodes(generateCodeRequest.getVoucherId(), generateCodeRequest.getNumber());
    }
}
