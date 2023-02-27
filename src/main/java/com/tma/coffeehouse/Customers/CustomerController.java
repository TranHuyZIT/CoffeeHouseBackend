package com.tma.coffeehouse.Customers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    @GetMapping("/{id}")
    public Customer findByID(@PathVariable long id){
        return customerService.findOne(id);
    }
    @PostMapping
    public ResponseEntity<Customer> insert(@RequestParam("") String address,
                                 @RequestParam("image")MultipartFile image,
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
