package com.example.ImageService.Repositories;

import com.example.ImageService.Entities.ProductCategoryImage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


public interface ProductCategoryImageRepository extends MongoRepository<ProductCategoryImage, String> {
}
