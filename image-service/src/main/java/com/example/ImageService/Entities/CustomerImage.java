package com.example.ImageService.Entities;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "customer_image")
@Builder
@Data
public class CustomerImage {
    @Id
    private String id;
    private String image;
}