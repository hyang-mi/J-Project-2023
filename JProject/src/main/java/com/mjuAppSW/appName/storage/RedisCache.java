package com.mjuAppSW.appName.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisCache {

    private final RedisTemplate<String, String> redisTemplate;

    public String createCertifyNum(Long memberId) {
        int random = ThreadLocalRandom.current().nextInt(100000, 1000000);
        String result = String.valueOf(random);
        redisTemplate.opsForValue().set("C" + memberId, result, 7, TimeUnit.MINUTES);
        log.info("create certify number from cache");
        return result;
    }

    public String getCertifyNum(Long memberId) {
        log.info("get certify number from Cache");
        return redisTemplate.opsForValue().get("C" + memberId);
    }

    public void removeCertifyNum(Long memberId) {
        redisTemplate.delete("C" + memberId);
        log.info("delete certify number from cache");
    }
}
