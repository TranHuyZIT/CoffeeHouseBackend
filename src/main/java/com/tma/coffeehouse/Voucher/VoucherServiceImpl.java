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
@Setter
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepository voucherRepository;
    private VoucherMapper voucherMapper;
    private final CartService cartService;
    private final AddVoucherMapper addVoucherMapper;
    public Page<VoucherDTO> findAll(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        Page<Voucher> page = voucherRepository.findAll(pageable);
        List<VoucherDTO> voucherDTO = voucherMapper.modelsTODTOS(
                page.getContent()
        );
        Page<VoucherDTO> pageDTO = page.map(voucherMapper::modelTODto);
        return pageDTO;
    }
    public VoucherDTO findById(Long id){
        return voucherMapper.modelTODto(
            voucherRepository.findById(id).orElseThrow(()-> new CustomException("Không tìm thấy voucher có id là " + id, HttpStatus.NOT_FOUND))
        );
    }
    Page<VoucherDTO> findAllAvailable(){
        return null;
    }
    public VoucherDTO insert(AddVoucherDTO addVoucherDTO){
        Voucher voucher = addVoucherMapper.dtoTOModel(addVoucherDTO);
        return voucherMapper.modelTODto(voucher);
    }
    public VoucherDTO update(long id, AddVoucherDTO addVoucherDTO){
        Voucher current = voucherRepository.findById(id)
                .orElseThrow(()-> new CustomException("Không tìm thấy voucher có id là " + id, HttpStatus.NOT_FOUND));
        current.setEndDate(addVoucherDTO.getEndDate());
        current.setLimitNumber(addVoucherDTO.getLimitNumber());
        current.setStartDate(addVoucherDTO.getStartDate());
        current.setPercentage(addVoucherDTO.getPercentage());
        current.setMaxDiscount(addVoucherDTO.getMaxDiscount());
        current.setMinOrderTotal(addVoucherDTO.getMinOrderTotal());
        Voucher saved = voucherRepository.save(current);
        return voucherMapper.modelTODto(saved);
    }
    public Set<VoucherDTO> findAllVoucherForCart(Long customerId){
        Set<Voucher> allVouchers = voucherRepository.findAllAvailable(CustomUtils.getDateWithoutTime());
        GetFullCartDTO cart = cartService.findOne(customerId);
        Long cartTotal = cartService.calculateCartTotal(cart);

        Set<VoucherDTO> validVouchers = new HashSet<>();
        Set<Product> productsOfCart = cart.getDetails().stream()
                .map((DetailOfCartDTO::getProduct)).collect(Collectors.toSet());

        for(Voucher voucher: allVouchers) {
            if (cartTotal < voucher.getMinOrderTotal()) continue;
            boolean isValid = true;
//            for (DetailOfCartDTO cartDetail: cart.getDetails()){
//                if (!voucher.getProducts().contains(cartDetail.getProduct())){
//                    isValid = false;
//                    break;
//                }
//            }
            for(Product product: voucher.getProducts()){
                if (!productsOfCart.contains(product)){
                    isValid = false;
                    break;
                }
            }
            if (isValid) validVouchers.add(voucherMapper.modelTODto(voucher));
        }
        return validVouchers;
    }
    public VoucherDTO delete(Long id){
        Voucher current = voucherRepository.findById(id)
                .orElseThrow(()-> new CustomException("Không tìm thấy voucher có id là " + id, HttpStatus.NOT_FOUND));
        voucherRepository.delete(current);
        return voucherMapper.modelTODto(current);
    }
}
