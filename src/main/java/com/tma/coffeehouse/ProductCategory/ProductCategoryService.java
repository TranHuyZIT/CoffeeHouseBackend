package com.tma.coffeehouse.ProductCategory;

import com.tma.coffeehouse.Utils.CustomUtils;
import com.tma.coffeehouse.ExceptionHandling.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;

    public List<ProductCategory> findAll(){
        return new ArrayList<>(productCategoryRepository.findAll());
    }
    public ProductCategory findById(long id) {return productCategoryRepository.findById(id).orElseThrow(()->new CustomException("Không tìm thấy loại sản phẩm với mã " + id, HttpStatus.NOT_FOUND));}
    public ProductCategory insert(String name, MultipartFile multipartFile) {
        ProductCategory cate = new ProductCategory();

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        cate.setImage(fileName);
        cate.setName(name);
        ProductCategory saved =  productCategoryRepository.save(cate);
        String uploadDir = "./src/main/resources/static/prod-category-img/" + saved.getId();
        CustomUtils.uploadFileToDirectory(uploadDir, multipartFile);
        return saved;
    }
    public ProductCategory update (Long id, String name, MultipartFile multipartfile){
        ProductCategory currentCategory = productCategoryRepository.findById(id)
                .orElseThrow(() -> new CustomException("Không tìm thấy danh mục sản phẩm với mã " + id, HttpStatus.NOT_FOUND));
        // Delete current image of category
        CustomUtils.deleteFile("./src/main/resources/static/prod-category-img/" + currentCategory.getId() + "/" + currentCategory.getImage());
        currentCategory.setName(name);
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartfile.getOriginalFilename()));
        currentCategory.setImage(fileName);
        ProductCategory saved = productCategoryRepository.save(currentCategory);
        // Update new image for this category
        String uploadDir = "./src/main/resources/static/prod-category-img/" + saved.getId();
        CustomUtils.uploadFileToDirectory(uploadDir, multipartfile);
        return saved;
    }

    public ProductCategory delete (Long id){
        ProductCategory currentCategory = productCategoryRepository.findById(id)
                .orElseThrow(() -> new CustomException("Không tìm thấy danh mục sản phẩm với mã " + id, HttpStatus.NOT_FOUND));
        productCategoryRepository.deleteById(id);
        String imageDir = "./src/main/resources/static/prod-category-img/" + currentCategory.getId();
        File file = new File(imageDir);
        CustomUtils.deleteDirectory(file);
        return currentCategory;
    }
    public byte[] getProductCategoryImage(Long id) {
        ProductCategory currentCategory = productCategoryRepository.findById(id)
                .orElseThrow(() -> new CustomException("Không tìm thấy danh mục sản phẩm với mã " + id, HttpStatus.NOT_FOUND));
        try{
            return Files.readAllBytes(Paths.get("./src/main/resources/static/prod-category-img/" + id +"/" + currentCategory.getImage() ));
        }
        catch (IOException exception){
            throw new CustomException("Không tìm thấy hình ảnh cho danh mục có id " + id, HttpStatus.NOT_FOUND);
        }
    }

}
