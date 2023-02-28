package com.tma.coffeehouse.Order.Mapper;

import com.tma.coffeehouse.Customers.Customer;
import com.tma.coffeehouse.Customers.CustomerRepository;
import com.tma.coffeehouse.Customers.CustomerService;
import com.tma.coffeehouse.ExceptionHandling.CustomException;
import com.tma.coffeehouse.Order.DTO.AddOrderDTO;
import com.tma.coffeehouse.Order.Order;
import com.tma.coffeehouse.Voucher.VoucherRepository;
import com.tma.coffeehouse.Voucher.VoucherService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@Mapper(componentModel = "spring", imports = {CustomException.class, HttpStatus.class})
public abstract class AddOrderMapper {
    @Autowired
    protected VoucherRepository voucherRepository;
    @Autowired
    protected CustomerRepository customerRepository;
    public abstract AddOrderDTO modelTODto(Order order);
    @Mappings({
            @Mapping(
                    target = "voucher",
                    expression = "java(addOrderDTO.getVoucherId() == null ? null : voucherRepository.findById(addOrderDTO.getVoucherId()).orElseThrow(()-> new CustomException(\"Không tìm thấy voucher có id là \" + addOrderDTO.getVoucherId(), HttpStatus.NOT_FOUND)))"),
            @Mapping(
                    target = "customer",
                    expression = "java(customerRepository.findById(addOrderDTO.getCustomerId()).orElseThrow(()-> new CustomException(\"Không tìm thấy khách hàng có id là \" + addOrderDTO.getCustomerId(), HttpStatus.NOT_FOUND)))")
    })
    public abstract Order dtoTOModel(AddOrderDTO addOrderDTO);
}
