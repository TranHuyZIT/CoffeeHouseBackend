package com.example.ImageService.Repositories;

import com.example.ImageService.Entities.ProductImage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

public interface ProductImageRepository extends MongoRepository<ProductImage, String> {
}
