package com.tma.coffeehouse.Voucher;

import com.tma.coffeehouse.Cart.CartService;
import com.tma.coffeehouse.Cart.DTO.GetFullCartDTO;
import com.tma.coffeehouse.CartDetails.DTO.DetailOfCartDTO;
import com.tma.coffeehouse.ExceptionHandling.CustomException;
import com.tma.coffeehouse.Product.Product;
import com.tma.coffeehouse.Utils.CustomUtils;
import com.tma.coffeehouse.Voucher.DTO.AddVoucherDTO;
import com.tma.coffeehouse.Voucher.DTO.VoucherDTO;
import com.tma.coffeehouse.Voucher.Mapper.AddVoucherMapper;
import com.tma.coffeehouse.Voucher.Mapper.VoucherMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepository voucherRepository;
    private final VoucherMapper voucherMapper;
    private final CartService cartService;
    private final AddVoucherMapper addVoucherMapper;
    public Page<Voucher> findAll(Integer pageNo, Integer pageSize, String sortBy, Boolean reverse) {
        if (pageNo == -1) {
            Pageable pageRequest = PageRequest.of(0, Integer.MAX_VALUE,
                    Sort.by(reverse? Sort.Direction.DESC : Sort.Direction. ASC, sortBy));
            return voucherRepository.findAll(pageRequest);
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        return voucherRepository.findAll(pageable);
    }

    public VoucherDTO findById(Long id){
        return voucherMapper.modelTODto(
            voucherRepository.findById(id).orElseThrow(()-> new CustomException("Không tìm thấy voucher có id là " + id, HttpStatus.NOT_FOUND))
        );
    }
    public VoucherDTO insert(AddVoucherDTO addVoucherDTO){
        Voucher voucher = addVoucherMapper.dtoTOModel(addVoucherDTO);
        voucherRepository.save(voucher);
        return voucherMapper.modelTODto(voucher);
    }
    public VoucherDTO update(long id, AddVoucherDTO addVoucherDTO){
        System.out.println(addVoucherDTO);
        Voucher current = voucherRepository.findById(id)
                .orElseThrow(()-> new CustomException("Không tìm thấy voucher có id là " + id, HttpStatus.NOT_FOUND));
        Voucher newVoucher = addVoucherMapper.dtoTOModel(addVoucherDTO);
        current.setEndDate(newVoucher.getEndDate());
        current.setLimitNumber(newVoucher.getLimitNumber());
        current.setStartDate(newVoucher.getStartDate());
        current.setPercentage(newVoucher.getPercentage());
        current.setMaxDiscount(newVoucher.getMaxDiscount());
        current.setMinOrderTotal(newVoucher.getMinOrderTotal());
        current.setProducts(newVoucher.getProducts());
        Voucher saved = voucherRepository.save(current);
        return voucherMapper.modelTODto(saved);
    }
    public VoucherDTO delete(Long id){
        Voucher currentVoucher = voucherRepository.findById(id).orElseThrow(
                () -> new CustomException("Không tìm thấy voucher có mã là" + id, HttpStatus.NOT_FOUND)
        );
        currentVoucher.setProducts(new HashSet<>());
        voucherRepository.save(currentVoucher);
        voucherRepository.delete(currentVoucher);
        return voucherMapper.modelTODto(currentVoucher);
    }
    public Set<VoucherDTO> findAllVoucherForCart(Long customerId){
        Set<Voucher> allVouchers = voucherRepository.findAllAvailable(CustomUtils.getDateWithoutTime());
        GetFullCartDTO cart = cartService.findOne(customerId);
        Long cartTotal = cart.getTongtien();
        System.out.println(allVouchers);
        System.out.println(cart);
        Set<VoucherDTO> validVouchers = new HashSet<>();
        Set<Product> productsOfCart = cart.getDetails().stream()
                .map((DetailOfCartDTO::getProduct)).collect(Collectors.toSet());

        // Apply a voucher only if a cart contains all the product in the vouchers
        for(Voucher voucher: allVouchers) {
            if (cartTotal < voucher.getMinOrderTotal()) continue;
            boolean isValid = true;
            for(Product product: voucher.getProducts()){
                if (!productsOfCart.contains(product)){
                    System.out.println(product);
                    isValid = false;
                    break;
                }
            }
            if (isValid) validVouchers.add(voucherMapper.modelTODto(voucher));
        }
        return validVouchers;
    }
}
