package com.example.ImageService.Entities;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "product_category_image")
@Builder
@Data
public class ProductCategoryImage {
    @Id
    private String id;
    private String image;
}