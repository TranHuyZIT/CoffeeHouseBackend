package com.tma.coffeehouse.Customers;

import com.tma.coffeehouse.ExceptionHandling.CustomException;
import com.tma.coffeehouse.User.User;
import com.tma.coffeehouse.User.UserRepository;
import com.tma.coffeehouse.Utils.CustomUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    public Customer insert(String address, Long id, MultipartFile multipartFile){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException("Không tìm thấy tài khoản với mã là " + id, HttpStatus.NOT_FOUND));
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        Customer newCustomer = Customer.builder()
                .address(address)
                .user(user)
                .image(fileName)
                .build();
        newCustomer = customerRepository.save(newCustomer);
        String uploadDir = "./src/main/resources/static/customer-img/" + newCustomer.getId();
        CustomUtils.uploadFileToDirectory(uploadDir, multipartFile);
        return newCustomer;
    }
    public Customer update(Long id, String address, MultipartFile multipartFile){
        Customer currentCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomException("Không thể tìm thấy thông tin khách hàng với mã" + id, HttpStatus.NOT_FOUND));
        String currentDir = "./src/main/resources/static/customer-img/" + currentCustomer.getId() ;
        CustomUtils.deleteFile(currentDir + "/" + currentCustomer.getImage());
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        CustomUtils.uploadFileToDirectory(currentDir, multipartFile);
        Customer newCustomer =  Customer.builder()
                .image(fileName)
                .user(currentCustomer.getUser())
                .address(address)
                .id(currentCustomer.getId())
                .build();
        return customerRepository.save(newCustomer);
    }
    public Customer findOne(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new CustomException("Không thể tìm thấy thông tin khách hàng với mã" + id, HttpStatus.NOT_FOUND));
    }
}
