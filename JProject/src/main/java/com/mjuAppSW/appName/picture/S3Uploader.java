package com.mjuAppSW.appName.domain.member.picture;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;


import java.nio.ByteBuffer;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    public boolean putPicture(String key, byte[] pictureBytes) {
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

    public byte[] getPicture(String memberId) {
        byte[] pictureBytes = null;

        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(memberId + "_profile_picture")
                    .build();

            ResponseBytes<GetObjectResponse> responseBytes = s3Client.getObjectAsBytes(getObjectRequest);
            pictureBytes = responseBytes.asByteArray();
            log.info("Picture downloaded successfully from S3");
        }
        catch (S3Exception e) {
            log.error("Error downloading picture from S3: " + e.getMessage());
        }
        return pictureBytes;
    }
}
