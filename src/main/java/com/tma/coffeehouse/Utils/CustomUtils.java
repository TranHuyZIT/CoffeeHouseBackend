package com.tma.coffeehouse.Utils;

import com.tma.coffeehouse.ExceptionHandling.CustomException;
import com.tma.coffeehouse.config.MQConfig.MQConfig;
import com.tma.coffeehouse.config.MQConfig.QueueMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomUtils {
    private final RabbitTemplate template;
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
    public static Date getDateWithoutTime() {
        try{
            SimpleDateFormat formatter = new SimpleDateFormat(
                    "dd/MM/yyyy");
            return formatter.parse(formatter.format(new Date()));
        }
        catch (ParseException e){
            throw new CustomException("Không thể lấy thời gian hiện tại", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public static void copyFileToDirectory(String dirToCopy, String dirDestination){
        Path fileToCopy = Paths.get(dirToCopy);
        Path destination = Paths.get(dirDestination);
        try {
            if (!Files.exists(destination)){
                Files.createDirectories(destination);
            }
            Files.copy(fileToCopy, destination.resolve(fileToCopy.getFileName()), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
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

    public void pushEmailMessageQueue(String subject, String toMail, String body){
        QueueMessage email = new QueueMessage(UUID.randomUUID().toString(), subject, toMail, body, new Date());
        template.convertAndSend(MQConfig.EXCHANGE, MQConfig.ROUTING_KEY, email);
    }

}
