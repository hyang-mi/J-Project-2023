package com.mjuAppSW.appName.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisStorage {
    private final RedisTemplate<String, String> redisTemplate;

    public void setCertifyNum(Long memberId, int certifyNum) {
        redisTemplate.opsForValue().set(String.valueOf(memberId), String.valueOf(certifyNum), 420, TimeUnit.SECONDS);
    }

    public String getCertifyNum(Long memberId) {
        return redisTemplate.opsForValue().get(String.valueOf(memberId));
    }

}
