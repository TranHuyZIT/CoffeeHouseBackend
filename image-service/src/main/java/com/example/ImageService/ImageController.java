package com.example.ImageService;

import com.example.ImageService.Entities.ProductImage;
import com.example.ImageService.Repositories.ProductImageRepository;
import com.mongodb.client.gridfs.model.GridFSFile;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/api/v1/image")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;
    @GetMapping(value = "/{type}/{id}" , produces = IMAGE_PNG_VALUE)
    public byte[] getImage(@PathVariable("type") String type , @PathVariable("id") String id){
        return imageService.getImage(type, id);
    }
    @GetMapping(value = "/product/image-zip")
    public ResponseEntity<byte[]> zipDownload() throws IOException {
        byte[] zipBytes = imageService.zipDownload();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "image.zip");
        return new ResponseEntity<>(zipBytes, headers, HttpStatus.OK);
    }
    @PostMapping(
        value = "/",
        consumes = {MediaType.APPLICATION_JSON_VALUE,
                MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public String addImage(@RequestPart(value="image") MultipartFile image,
                           @RequestPart(value = "type") String type) throws Exception {
        return imageService.addImage(image, type);
    }
    @PostMapping(value = "/image-url")
    public String addImageFromUrl(@RequestPart(value = "type") String type,
                                  @RequestPart(value = "name") String name,
                                  @RequestPart(value = "imageUrl") String imageURl){
        return imageService.addImage(imageURl, type, name);
    }
    @PutMapping(value = "{type}/{id}",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE,}
    )
    public String updateImage(@PathVariable("type") String type,
                              @PathVariable("id") String id, @RequestPart(value="image") MultipartFile image){
        return imageService.updateImage(type, id, image);
    }
}
