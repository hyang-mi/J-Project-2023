package com.mjuAppSW.joA.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@EnableScheduling
@Configuration
public class CertifyNumConfig {

    @Bean
    Map<Long, String> certifyNumMap() {
        return new ConcurrentHashMap<>();
    }

}
