package com.tma.coffeehouse.Voucher.Mapper;

import com.tma.coffeehouse.ExceptionHandling.CustomException;
import com.tma.coffeehouse.Product.Product;
import com.tma.coffeehouse.Product.ProductRepository;
import com.tma.coffeehouse.Voucher.DTO.AddVoucherDTO;
import com.tma.coffeehouse.Voucher.Voucher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AddVoucherMapper {
    private final ProductRepository productRepository;
    public Voucher dtoTOModel(AddVoucherDTO addVoucherDTO){
        if (addVoucherDTO == null) return null;
        Voucher.VoucherBuilder voucherBuilder = Voucher.builder();
        voucherBuilder.startDate(addVoucherDTO.getStartDate());
        voucherBuilder.name(addVoucherDTO.getName());
        voucherBuilder.remainingNumber(addVoucherDTO.getLimitNumber());
        voucherBuilder.endDate(addVoucherDTO.getEndDate());
        voucherBuilder.limitNumber(addVoucherDTO.getLimitNumber());
        voucherBuilder.maxDiscount(addVoucherDTO.getMaxDiscount());
        voucherBuilder.minOrderTotal(addVoucherDTO.getMinOrderTotal());
        voucherBuilder.percentage(addVoucherDTO.getPercentage());
        Set<Product> products = new HashSet<>();
        System.out.println(Arrays.toString(addVoucherDTO.getProductsId()));
        for (Long prodId : addVoucherDTO.getProductsId()){
            Product product = productRepository.findById(prodId)
                    .orElseThrow(()->new CustomException("Không tìm thấy sản phẩm với mã là" + prodId, HttpStatus.NOT_FOUND));
            products.add(product);
        }
        voucherBuilder.products(products);
        return voucherBuilder.build();
    }
}
