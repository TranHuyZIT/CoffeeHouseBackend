package com.tma.coffeehouse.Product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tma.coffeehouse.Product.DTO.AddProductRequest;
import com.tma.coffeehouse.Product.DTO.ProductDTO;
import com.tma.coffeehouse.Product.DTOMapper.AddProductMapper;
import com.tma.coffeehouse.Product.DTOMapper.ProductMapper;
import com.tma.coffeehouse.ProductCategory.ProductCategoryService;
import com.tma.coffeehouse.Topping.Topping;
import com.tma.coffeehouse.Topping.ToppingService;
import com.tma.coffeehouse.Utils.CustomUtils;
import com.tma.coffeehouse.ExceptionHandling.CustomException;
import com.tma.coffeehouse.ProductCategory.ProductCategory;
import com.tma.coffeehouse.Utils.ImageService;
import com.tma.coffeehouse.config.Cache.CacheData;
import com.tma.coffeehouse.config.Cache.CacheRepository;
import com.tma.coffeehouse.config.Cache.CacheService;
import com.tma.coffeehouse.config.Serializer.PageModule;
import com.tma.coffeehouse.config.WebClientConfig;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final AddProductMapper addProductMapper;
    private final ProductMapper productMapper;
    private final CacheRepository cacheDataRepository;
    private final CacheService cacheService;
    private final ImageService imageService;

    private final ObjectMapper objectMapper;

    @Lazy
    @Autowired
    private ToppingService toppingService;
    private final ProductCategoryService productCategoryService;
    Page<Product> findAll(Long productCategoryID, String name, Integer pageNo, Integer pageSize, String sortBy, Boolean reverse)  {
        String key = "product:GetAll" +"_category:" + productCategoryID
                + "_name:" + name + "_pageNo:" + pageNo + "_pageSize: " + pageSize +
                "_sortBy:" + sortBy + "_reverse:" + reverse;
        objectMapper.registerModule(new PageModule());
        Optional<CacheData> optionalCacheData = cacheDataRepository.findById(key);
        if (optionalCacheData.isPresent()){
            String pageProductAsString = optionalCacheData.get().getValue();
            TypeReference<Page<Product>> mapType = new TypeReference<>() {
            };
            try{
                return objectMapper.readValue(pageProductAsString, mapType);
            }
            catch (JsonProcessingException ignored){}
        }
        if (pageNo == -1) {
            Pageable pageAndSortingRequest = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(reverse? Sort.Direction.DESC : Sort.Direction. ASC, sortBy));
            Page<Product> results =  productRepository.findAllByQueries(productCategoryID, name, pageAndSortingRequest);
            cacheService.writeCache(key, results);
            return results;
        }
        Pageable pageAndSortingRequest = PageRequest.of(pageNo, pageSize, Sort.by(reverse? Sort.Direction.DESC : Sort.Direction. ASC, sortBy));
        Page<Product> results =  productRepository.findAllByQueries(productCategoryID, name, pageAndSortingRequest);
        cacheService.writeCache(key, results);
        return results;
    }
    List<Product> getAllProductsByImages(String[] images){
        return productRepository.findAllByImages(images);
    }
    byte[] getImage(Long id){
        Product currentProd = productRepository.findById(id)
                .orElseThrow(() -> new CustomException("Không tìm thấy sản phẩm với mã " + id, HttpStatus.NOT_FOUND));
        try{
            return Files.readAllBytes(Paths.get("./src/main/resources/static/prod-img/" + id + "/" +  currentProd.getImage()));
        }
        catch(IOException e){
            throw new CustomException("Có lỗi xảy ra khi lấy hình ảnh sản phẩm", HttpStatus.NOT_FOUND);
        }
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

        // Sending image to Image Service
        String imageId =  imageService.insertImage("product", multipartFile);
        newProd.setImage(imageId);
        cacheService.destroyCache("product");
        Product saved =  productRepository.save(newProd);

        // Binding toppings to new product
        for (Topping topping : newProd.getProductToppings()){
            topping.addProduct(saved);
        }
        return productMapper.modelTODto(saved);
    }
    @Transactional(rollbackOn = {CustomException.class, Exception.class, Throwable.class})
    public ProductDTO update (Long id, AddProductRequest productRequest,  MultipartFile multipartFile){
        Product currentProd = productRepository.findById(id)
                .orElseThrow(() -> new CustomException("Không tìm thấy sản phẩm với mã " + id, HttpStatus.NOT_FOUND));
        currentProd.setName(productRequest.getName());
        currentProd.setPrice(productRequest.getPrice());
        currentProd.setDescription(productRequest.getDescription());
        ProductCategory cate = productCategoryService.findById(productRequest.getProductCategoryId());
        currentProd.setProductCategory(cate);
        Set<Topping> toppingsOfProduct = toppingService.findManyByIDs(productRequest.getToppingsId());
        currentProd.setProductToppings(toppingsOfProduct);

        // If not having a new image from request to update, then return here.
        if (multipartFile == null){
            Product saved =  productRepository.save(currentProd);
            for (Topping topping : toppingsOfProduct){
                topping.addProduct(saved);
            }
            return productMapper.modelTODto(saved);
        }
        imageService.updateImage(currentProd.getImage(), "product", multipartFile);
        cacheService.destroyCache("product");
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
