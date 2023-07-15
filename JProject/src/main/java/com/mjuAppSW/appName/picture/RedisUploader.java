package com.mjuAppSW.appName.picture;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisUploader {

    private final RedisTemplate<String, byte[]> redisTemplate;
    private final S3Uploader s3Uploader;

    public void updatePicture(Long memberId, String base64Picture) {
        byte[] pictureBytes = Base64.getDecoder().decode(base64Picture);
        savePicture(String.valueOf(memberId), pictureBytes);
    }

    public String bringPicture(Long memberId) {
        String key = String.valueOf(memberId);
        byte[] pictureBytes;
        if (containsKey(key)) {
            pictureBytes = getValue(key);
        }
        else {
            pictureBytes = s3Uploader.getPicture(key);
            savePicture(key, pictureBytes);
        }
        return Base64.getEncoder().encodeToString(pictureBytes);
    }

    public void savePicture(String key, byte[] pictureBytes) {
        redisTemplate.opsForValue().set(key, pictureBytes);
    }

    public boolean containsKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public Set<String> getAllKeys() {
        return redisTemplate.keys("*");
    }

    public byte[] getValue(String key) {
        Object object = redisTemplate.opsForValue().get(key);
        if (object == null)
            return null;
        else
            return object.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Scheduled(fixedRate = 60000)
    public void putAndDelete() {
        Set<String> keys = getAllKeys();
        for (String key : keys) {
            s3Uploader.putPicture(key, getValue(key));
        }
        redisTemplate.delete(keys);
    }
}
