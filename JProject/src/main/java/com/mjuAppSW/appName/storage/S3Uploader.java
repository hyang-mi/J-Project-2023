package com.mjuAppSW.appName.picture;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;


import java.nio.ByteBuffer;
import java.util.Base64;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    public boolean putPicture(String key, String base64Picture) {
        byte[] pictureBytes = Base64.getDecoder().decode(base64Picture);
        ByteBuffer byteBuffer = ByteBuffer.wrap(pictureBytes);
        boolean result = false;

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key + "_profile_picture")
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromByteBuffer(byteBuffer));
            result = true;
            log.info("Picture uploaded successfully to S3");
        }
        catch (S3Exception e) {
            log.error("Error uploading picture to S3: " + e.getMessage());
        }
        finally {
            return result;
        }
    }

    public String getPicture(String memberId) {
        String pictureString = null;
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(memberId + "_profile_picture")
                    .build();

            ResponseBytes<GetObjectResponse> responseBytes = s3Client.getObjectAsBytes(getObjectRequest);
            byte[] pictureBytes = responseBytes.asByteArray();
            pictureString = Base64.getEncoder().encodeToString(pictureBytes);
            log.info("Picture downloaded successfully from S3");
        }
        catch (S3Exception e) {
            log.error("Error downloading picture from S3: " + e.getMessage());
        }
        finally {
            return pictureString;
        }
    }

//    public boolean deletePicture(String memberId) {
//        try {
//            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
//                    .bucket(bucketName)
//                    .key(memberId + "_profile_picture")
//                    .build();
//
//            log.info("Picture deleted successfully from S3");
//            s3Client.deleteObject(deleteObjectRequest);
//        }
//        catch (S3Exception e) {
//            log.error("Error deleting picture from S3: " + e.getMessage());
//            return false;
//        }
//        return true;
//    }
}
