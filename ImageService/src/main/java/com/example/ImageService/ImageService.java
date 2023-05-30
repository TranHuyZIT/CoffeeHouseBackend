package com.example.ImageService;

import com.example.ImageService.Entities.CustomerImage;
import com.example.ImageService.Entities.ProductCategoryImage;
import com.example.ImageService.Entities.ProductImage;
import com.example.ImageService.Repositories.CustomerImageRepository;
import com.example.ImageService.Repositories.ProductCategoryImageRepository;
import com.example.ImageService.Repositories.ProductImageRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.MongoDbFactoryParser;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ProductImageRepository productImageRepository;
    private final CustomerImageRepository customerImageRepository;
    private final ProductCategoryImageRepository productCategoryImageRepository;
    private final GridFsTemplate gridFsTemplate;
    private final MongoDatabaseFactory mongoDatabaseFactory;
    private GridFSBucket getGridFs() {

        MongoDatabase db = mongoDatabaseFactory.getMongoDatabase();
        return GridFSBuckets.create(db);
    }
    public byte[] findOneAndConvertToBytes (String fileId){
        GridFSFile file = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
        GridFsResource resource =  new GridFsResource(file, getGridFs().openDownloadStream(file.getObjectId()));
        try{
            return resource.getContentAsByteArray();
        }
        catch (IOException e){
            return null;
        }
    }
    public String addImage(MultipartFile image, String type)  {
        String gridFSId;
        try{
            DBObject metadata = new BasicDBObject();
            metadata.put("type", type);
            gridFSId =
                    gridFsTemplate.
                            store(image.getInputStream(), Objects.requireNonNull(image.getOriginalFilename()), metadata)
                            .toString();
        }
        catch (Exception e){
            throw new CustomException("Lỗi ảnh", HttpStatus.BAD_REQUEST);
        }
        return this.saveImage(type, gridFSId);
    }
    public String addImage(String imageUrl, String type, String name){
        String gridFsId;
        try{
            InputStream inputStream = new URL(imageUrl).openStream();
            DBObject metadata = new BasicDBObject();
            metadata.put("type", type);
            gridFsId = gridFsTemplate.store(
                    inputStream, name, metadata
            ).toString();
        }
        catch (Exception e){
            throw new CustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return this.saveImage(type, gridFsId);
    }

    public String saveImage(String type, String gridFSId){
        switch (type) {
            case "product" -> {
                ProductImage productImage = ProductImage.builder()
                        .image(gridFSId)
                        .build();
                productImage = productImageRepository.save(productImage);
                return productImage.getId();
            }
            case "customer" -> {
                CustomerImage customerImage = CustomerImage.builder()
                        .image(gridFSId)
                        .build();
                customerImage = customerImageRepository.save(customerImage);
                return customerImage.getId();
            }
            case "product_category" -> {
                ProductCategoryImage productCategoryImage = ProductCategoryImage.builder()
                        .image(gridFSId)
                        .build();
                productCategoryImage = productCategoryImageRepository.save(productCategoryImage);
                return productCategoryImage.getId();
            }
            default -> {
                return null;
            }
        }
    }

    public byte[] getImage(String type ,String id) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        switch (type) {
            case "product" -> {
                String gridFSId =  productImageRepository.findById(id).orElseThrow(
                        () -> new CustomException("Không tìm thấy ảnh này", HttpStatus.NOT_FOUND)
                ).getImage();
                return findOneAndConvertToBytes(gridFSId);
            }
            case "customer" -> {
                String gridFSId =  customerImageRepository.findById(id).orElseThrow(
                        () -> new CustomException("Không tìm thấy ảnh này", HttpStatus.NOT_FOUND)
                ).getImage();
                return findOneAndConvertToBytes(gridFSId);
            }
            case "product_category" -> {
                String gridFSId =  productCategoryImageRepository.findById(id).orElseThrow(
                        () -> new CustomException("Không tìm thấy ảnh này", HttpStatus.NOT_FOUND)
                ).getImage();
                return findOneAndConvertToBytes(gridFSId);
            }
            default -> {
                return null;
            }
        }
    }
    public String updateImage(String type, String id, MultipartFile image){
        String gridFSId;
        try{
            DBObject metadata = new BasicDBObject();
            metadata.put("type", type);
            gridFSId =
                    gridFsTemplate.
                            store(image.getInputStream(), Objects.requireNonNull(image.getOriginalFilename()), metadata)
                            .toString();
        }
        catch (Exception e){
            throw new CustomException("Lỗi ảnh", HttpStatus.BAD_REQUEST);
        }
        switch (type){
            case "product" -> {
                ProductImage productImage = productImageRepository.findById(id).orElseThrow(
                        () -> new CustomException("Không tìm tháy ảnh", HttpStatus.NOT_FOUND)
                );
                gridFsTemplate.delete(new Query(Criteria.where("_id").is(productImage.getImage())));
                productImage.setImage(gridFSId);
                return productImageRepository.save(productImage).getId();
            }
            case "customer" -> {
                CustomerImage customerImage = customerImageRepository.findById(id).orElseThrow(
                        () -> new CustomException("Không tìm tháy ảnh", HttpStatus.NOT_FOUND)
                );
                gridFsTemplate.delete(new Query(Criteria.where("_id").is(customerImage.getImage())));
                customerImage.setImage(gridFSId);
                return customerImageRepository.save(customerImage).getId();
            }
            case "product_category" -> {
                ProductCategoryImage productCategoryImage = productCategoryImageRepository.findById(id).orElseThrow(
                        () -> new CustomException("Không tìm tháy ảnh", HttpStatus.NOT_FOUND)
                );
                gridFsTemplate.delete(new Query(Criteria.where("_id").is(productCategoryImage.getImage())));
                productCategoryImage.setImage(gridFSId);
                return productCategoryImageRepository.save(productCategoryImage).getId();
            }
            default -> {
                return null;
            }
        }
    }
}
