package org.zydd.bebtpn.service;

import io.minio.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zydd.bebtpn.dto.ResponHeader;
import org.zydd.bebtpn.dto.ResponHeaderMessage;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
public class MinioService {

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Value("${minio.bucketName}")
    private String bucketName;

    private MinioClient minioClient;

    @PostConstruct
    public void initializeMinioClient() {
        minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
        createBucketIfNotExists();
    }

    public void createBucketIfNotExists() {
        try {
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while checking or creating bucket", e);
        }
    }

    public void uploadFile(String fileName, byte[] data) {
        try {
            InputStream inputStream = new ByteArrayInputStream(data);
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(inputStream, data.length, -1)
                            .contentType("image/jpeg")
                            .build()
            );
        } catch (Exception e) {
            ResponHeader header = ResponHeaderMessage.getBadRequestError();
            header.setMessage("Error while uploading file" + e.getMessage());
        }
    }

    public void deleteFile(String fileName) {
        try {
            // Delete file from MinIO bucket
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
        } catch (Exception e) {
            // Handle exception
            ResponHeader header = ResponHeaderMessage.getBadRequestError();
            header.setMessage("Error while deleting file" + e.getMessage());
        }
    }

    public String getObjectUrl(String objectName) {
        return endpoint + "/" + bucketName + "/" + objectName;
    }
}

