package com.tma.coffeehouse.Product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tma.coffeehouse.Product.DTO.AddProductRequest;
import com.tma.coffeehouse.Product.DTO.ProductDTO;
import com.tma.coffeehouse.Product.DTOMapper.AddProductMapper;
import com.tma.coffeehouse.Product.DTOMapper.ProductMapper;
import com.tma.coffeehouse.Topping.Topping;
import com.tma.coffeehouse.Topping.ToppingRepository;
import com.tma.coffeehouse.Topping.ToppingService;
import com.tma.coffeehouse.Utils.CustomUtils;
import com.tma.coffeehouse.ExceptionHandling.CustomException;
import com.tma.coffeehouse.ProductCategory.ProductCategory;
import com.tma.coffeehouse.ProductCategory.ProductCategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final AddProductMapper addProductMapper;
    private final ProductMapper productMapper;
    Page<Product> findAll(Long productCategoryID, String name, Integer pageNo, Integer pageSize, String sortBy){
        Pageable pageAndSortingRequest = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        return  productRepository.findAllByQueries(productCategoryID, name, pageAndSortingRequest);
    }
    ProductDTO findById(Long id){
        Product product = productRepository.findById(id).orElseThrow(()->new CustomException("Không tìm thấy sản phẩm với mã " + id, HttpStatus.NOT_FOUND));
        Set<Topping> toppings = product.getProductToppings();
        System.out.println(toppings);
        return productMapper.modelTODto(product);
    }
    public AddProductRequest getJsonAddProductRequest(String product){
        AddProductRequest productJSON;
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            productJSON = objectMapper.readValue(product, AddProductRequest.class);
        }catch (IOException err){
            throw new CustomException(err.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return  productJSON;
    }

    @Transactional(rollbackOn = {CustomException.class, Exception.class, Throwable.class})
    public ProductDTO insert(AddProductRequest productRequest, MultipartFile multipartFile) {
        Product newProd = addProductMapper.dtoTOModel(productRequest);

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        newProd.setImage(fileName);

        Product saved =  productRepository.save(newProd);
        for (Topping topping : newProd.getProductToppings()){
            topping.addProduct(saved);
        }
        String uploadDir = "./src/main/resources/static/prod-img/" + saved.getId();
        CustomUtils.uploadFileToDirectory(uploadDir, multipartFile);
        return productMapper.modelTODto(saved);
    }

    public ProductDTO update (Long id, AddProductRequest productRequest,  MultipartFile multipartFile){
        Product currentProd = productRepository.findById(id)
                .orElseThrow(() -> new CustomException("Không tìm thấy sản phẩm với mã " + id, HttpStatus.NOT_FOUND));
        CustomUtils.deleteFile("./src/main/resources/static/prod-img/" + currentProd.getId() + "/" + currentProd.getImage());
        currentProd.setName(productRequest.getName());
        currentProd.setPrice(productRequest.getPrice());
        currentProd.setDescription(productRequest.getDescription());

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        currentProd.setImage(fileName);
        String uploadDir = "./src/main/resources/static/prod-img/" + currentProd.getId();
        CustomUtils.uploadFileToDirectory(uploadDir, multipartFile);
        return productMapper.modelTODto(productRepository.save(currentProd));
    }

    public Product delete (Long id){
        Product currentProd = productRepository.findById(id).orElseThrow(() -> new CustomException("Không tìm thấy sản phẩm với mã " + id, HttpStatus.NOT_FOUND));
        productRepository.deleteById(id);
        String imageDir = "./src/main/resources/static/prod-img/" + currentProd.getId();
        File file = new File(imageDir);
        CustomUtils.deleteDirectory(file);
        return currentProd;
    }

    public Set<Product> findManyByIDs (Long[] prodIds){
        Set<Product> products = new HashSet<>();
        for(long id: prodIds){
            Product product = productRepository.findById(id)
                    .orElseThrow(()->new CustomException("Không tìm thấy sản phẩm có mã " + id, HttpStatus.NOT_FOUND));
            products.add(product);
        }
        return products;
    }

}
