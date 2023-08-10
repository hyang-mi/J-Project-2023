package com.mjuAppSW.appName.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
@Slf4j
public class CertifyCache {

    private final RedisTemplate<String, String> redisTemplate;

    public String createCertifyNum() {
        int random = ThreadLocalRandom.current().nextInt(100000, 1000000);
        log.info("create certify number from cache");
        return String.valueOf(random);
    }

    public String getCertifyNum(Long memberId) {
        log.info("get URL Code from Cache");
        return redisTemplate.opsForValue().get("C" + memberId);
    }

    public void removeCertifyNum(Long memberId) {
        redisTemplate.delete("C" + memberId);
        log.info("delete certify number from cache");
    }

    public void setURLCode(Long memberId, String URL) {
        log.info("set URL Code in Cache");
        redisTemplate.opsForValue().set("C" + memberId, URL);
    }

    public String getURLCode(Long memberId) {
        log.info("get URL Code from Cache");
        return redisTemplate.opsForValue().get("U" + memberId);
    }

    public String deleteURLCode(Long memberId) {
        log.info("delete URL code from Cache");
        String urlCode = getURLCode(memberId);
        redisTemplate.delete("U" + memberId);
        return urlCode;
    }
}
