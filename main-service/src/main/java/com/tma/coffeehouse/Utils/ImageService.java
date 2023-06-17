package com.tma.coffeehouse.Utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final WebClient webClient;
    @Value("${image.service.url}")
    private String ImageServiceURL;
    public String insertImage(String type, MultipartFile image){
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("type", type);
        bodyBuilder.part("image", image.getResource());

        return webClient.post().uri(ImageServiceURL)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
    public String insertImage(String type, String imageUrl, String name){
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("type", type);
        bodyBuilder.part("imageUrl", imageUrl);
        bodyBuilder.part("name", name);
        return webClient.post().uri(ImageServiceURL + "add-url")
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
    public String updateImage(String id, String type, MultipartFile image){
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("image", image.getResource());

        return  webClient.put().uri(ImageServiceURL + type + "/"+ id)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
