package com.tma.coffeehouse.Customers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<Customer> findAll(
            @RequestParam (defaultValue = "", name = "name", required = false) String name,
            @RequestParam(defaultValue = "0", name="pageNo", required = false) Integer pageNo,
            @RequestParam(defaultValue = "10", name="pageSize", required = false) Integer pageSize,
            @RequestParam(defaultValue = "createdAt", name="sortBy", required = false) String sortBy,
            @RequestParam(defaultValue = "true", name="reverse", required = false) boolean reverse
    ){
        return this.customerService.findAll(name, pageNo, pageSize, sortBy, reverse);
    }
    @GetMapping("/{id}")
    public Customer findByID(@PathVariable long id){
        return customerService.findOne(id);
    }
    @GetMapping(value = "/image/{id}" , produces = IMAGE_PNG_VALUE)
    public byte[] getImage(@PathVariable Long id){
        return customerService.getImage(id);
    }
    @PostMapping
    public ResponseEntity<Customer> insert(@RequestParam("") String address,
                                 @RequestParam(value = "image", required = false)MultipartFile image,
                                 @RequestParam("userId") Long id){
        return new ResponseEntity<Customer>(customerService.insert(address, id, image), HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@RequestParam("address") String address,
                                           @RequestParam("image")MultipartFile image,
                                           @PathVariable Long id){
        return new ResponseEntity<Customer>(customerService.update(id, address, image), HttpStatus.OK);
    }


}
