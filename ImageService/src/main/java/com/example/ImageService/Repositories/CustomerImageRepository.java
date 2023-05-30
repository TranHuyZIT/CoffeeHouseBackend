package com.example.ImageService.Repositories;

import com.example.ImageService.Entities.CustomerImage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

public interface CustomerImageRepository extends MongoRepository<CustomerImage, String> {
}
