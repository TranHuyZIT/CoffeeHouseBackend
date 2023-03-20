package com.tma.coffeehouse.Product;


import com.tma.coffeehouse.Product.DTO.AddProductRequest;
import com.tma.coffeehouse.Product.DTO.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<Product> getAllProduct(
            @RequestParam(name="prodCategoryID", defaultValue = "0") Long prodCategoryID,
            @RequestParam(name="name", defaultValue = "") String name,
            @RequestParam(defaultValue = "0", name="pageNo") Integer pageNo,
            @RequestParam(defaultValue = "10", name="pageSize") Integer pageSize,
            @RequestParam(defaultValue = "createdAt", name="sortBy") String sortBy,
            @RequestParam(defaultValue = "true", name="reverse") boolean reverse
    )
    {
        return productService.findAll(prodCategoryID, name, pageNo - 1, pageSize, sortBy, reverse);
    }
    @GetMapping(value = "/image/{id}", produces = IMAGE_PNG_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public byte[] getImage(@PathVariable Long id) {
        return productService.getImage(id);
    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO findById(@PathVariable long id){
        return productService.findById(id);
    }
    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE}
    )

    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO insertProduct(@RequestPart("image") MultipartFile multipartFile,
                                    @RequestPart("product") String productString){
        AddProductRequest product = productService.getJsonAddProductRequest(productString);

        return productService.insert(product, multipartFile);
    }

    @PutMapping(path = {"/{id}"}, consumes = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE})
    public ProductDTO updateProduct(@PathVariable long id,
                                                 @RequestPart(value = "image", required = false) MultipartFile multipartFile,
                                                 @RequestPart("product") String productString){
        AddProductRequest product = productService.getJsonAddProductRequest(productString);

        return productService.update(id, product, multipartFile);
    }

    @DeleteMapping({"/{id}"})
    public  ResponseEntity<Product> deleteProductCategory(@PathVariable long id){
        return new ResponseEntity<>(productService.delete(id), HttpStatus.OK);
    }

}
