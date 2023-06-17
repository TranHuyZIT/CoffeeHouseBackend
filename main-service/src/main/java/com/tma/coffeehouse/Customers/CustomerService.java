package com.tma.coffeehouse.Customers;

import com.tma.coffeehouse.ExceptionHandling.CustomException;
import com.tma.coffeehouse.User.User;
import com.tma.coffeehouse.User.UserRepository;
import com.tma.coffeehouse.Utils.CustomUtils;
import com.tma.coffeehouse.Utils.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    public byte[] getImage(Long id){
        Customer currentCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomException("Không thể tìm thấy thông tin khách hàng với mã" + id, HttpStatus.NOT_FOUND));
        try {
            return Files.readAllBytes(Paths.get("./src/main/resources/static/customer-img/" + id + "/" + currentCustomer.getImage()));
        }
        catch (IOException e) {
            throw new CustomException("Không thể lấy dữ liệu ảnh khách hàng", HttpStatus.NOT_FOUND);
        }
    }

    public Page<Customer> findAll(String name, Integer pageNo, Integer pageSize, String sortBy, boolean reverse){
        if (pageNo == -1) {
            Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(reverse? Sort.Direction.DESC : Sort.Direction.ASC, sortBy));
            return customerRepository.findAll(pageable);
        }
        Pageable pageable = PageRequest.of(pageNo,pageSize, Sort.by(reverse? Sort.Direction.DESC : Sort.Direction.ASC, sortBy));
        if (!Objects.equals(name, "")){
            return customerRepository.findByUser_NameContaining(name, pageable);
        }
        return customerRepository.findAll(pageable);
    }
    public Customer insert(String address, Long id, MultipartFile multipartFile){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException("Không tìm thấy tài khoản với mã là " + id, HttpStatus.NOT_FOUND));
        Customer newCustomer = Customer.builder()
                .address(address)
                .user(user)
                .image("")
                .build();
        if (multipartFile != null){
            String imageId = imageService.insertImage("customer", multipartFile);
            newCustomer.setImage(imageId);
        }
        newCustomer = customerRepository.save(newCustomer);
        return newCustomer;
    }
    public Customer update(Long id, String address, MultipartFile multipartFile){
        Customer currentCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomException("Không thể tìm thấy thông tin khách hàng với mã" + id, HttpStatus.NOT_FOUND));
        String fileName = currentCustomer.getImage();
        if (multipartFile != null){
            imageService.updateImage(currentCustomer.getImage(), "customer", multipartFile);
        }
        Customer newCustomer =  Customer.builder()
                .image(fileName)
                .user(currentCustomer.getUser())
                .address(address)
                .id(currentCustomer.getId())
                .build();
        return customerRepository.save(newCustomer);
    }
    public Customer findOne(Long id) {
        return customerRepository.findByUser_Id(id).orElseThrow(() -> new CustomException("Không thể tìm thấy thông tin khách hàng với mã" + id, HttpStatus.NOT_FOUND));
    }
}
