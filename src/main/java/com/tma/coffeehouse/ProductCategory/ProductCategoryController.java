package com.tma.coffeehouse.ProductCategory;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/v1/prod-category")
@RequiredArgsConstructor
public class ProductCategoryController {
    private final ProductCategoryService productCategoryService;
    @GetMapping
    public ResponseEntity<List<ProductCategory>> getAllProductCategory(){
        return new ResponseEntity<>(productCategoryService.findAll(), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<ProductCategory> insertProductCategory(@RequestParam("image") MultipartFile multipartFile,@RequestParam("name") String name) throws IOException {


        return new ResponseEntity<>(productCategoryService.insert(name, multipartFile), HttpStatus.OK);
    }

    @PutMapping({"/{id}"})
    public ResponseEntity<ProductCategory> updateProductCategory(@PathVariable long id, @RequestParam("image") MultipartFile multipartFile,@RequestParam("name") String name){
        return new ResponseEntity<>(productCategoryService.update(id, name, multipartFile), HttpStatus.OK);
    }

    @DeleteMapping({"/{id}"})
    public  ResponseEntity<ProductCategory> deleteProductCategory(@PathVariable long id){
        return new ResponseEntity<>(productCategoryService.delete(id), HttpStatus.OK);
    }
}
