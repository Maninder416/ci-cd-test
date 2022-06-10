package io.reactivestax.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;

@Service
@Slf4j
public class StorageService {

    @Value("${application.bucket.name}")
    private String bucket;

    @Autowired
    private AmazonS3 s3Client;

    public String uploadFile(MultipartFile file) {
        File fileObject = convertMultiPartFileToFile(file);
        String FileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucket, FileName, fileObject));
        fileObject.delete();
        return "file uploaded: " + FileName;
    }

    public byte[] downloadFile(String fileName) {
        S3Object s3Object = s3Client.getObject(bucket, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public String deleteFile(String fileName){
        s3Client.deleteObject(bucket,fileName);
        return "File "+fileName+" deleted";
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream outputStream = new FileOutputStream(convertedFile)) {
            outputStream.write(file.getBytes());
        } catch (Exception e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }
}
