package com.tma.coffeehouse.Utils;

import com.tma.coffeehouse.ExceptionHandling.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class CustomUtils {
    public static Boolean deleteDirectory (File file){
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDirectory(f);
            }
        }
        return file.delete();
    }
    public static Boolean deleteFile(String dir){
        File file = new File(dir);
        if (file != null){
            return file.delete();
        }
        return false;
    }
    public static Timestamp convertStringToDate(String dateString){
        try{
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Date date = formatter.parse(dateString);
            return new Timestamp(date.getTime());
        }catch (ParseException e){
            throw new CustomException("Không thể chuyển định dạng ngày tháng năm ", HttpStatus.BAD_REQUEST);
        }
    }
    public static void uploadFileToDirectory(String uploadDir, MultipartFile multipartFile){
        Path uploadPath = Paths.get(uploadDir);
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try{
            if (!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }
            InputStream inputStream = multipartFile.getInputStream();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }catch(IOException e){
            throw new CustomException("Có lỗi xảy ra khi upload file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
