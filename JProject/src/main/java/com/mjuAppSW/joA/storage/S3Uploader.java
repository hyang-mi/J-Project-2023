package com.mjuAppSW.joA.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;


@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    public String putPicture(Long memberId, String base64Picture) {
        String key = String.valueOf(memberId);
        byte[] pictureBytes = Base64.getDecoder().decode(base64Picture);
        ByteBuffer byteBuffer = ByteBuffer.wrap(pictureBytes);
        String result = "error";
        int random = ThreadLocalRandom.current().nextInt(10000000, 100000000);

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key + ":" + random)
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromByteBuffer(byteBuffer));
            log.info("Picture uploaded successfully to S3");
            result = key + ":" + random;
        }
        catch (S3Exception e) {
            log.error("Error uploading picture to S3: " + e.getMessage());
        }
        finally {
            return result;
        }
    }

    public boolean deletePicture(String key) {
        if(key == null) return true;
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
            log.info("Picture deleted successfully from S3");
            return true;
        }
        catch (Exception e) {
            log.error("Error deleting picture from S3: " + e.getMessage());
            return false;
        }
    }
}
