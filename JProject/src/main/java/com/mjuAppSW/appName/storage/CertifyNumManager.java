package com.mjuAppSW.appName.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
@Slf4j
public class CertifyNumManager {

    private final Map<Long, String> certifyNumMap;

    public String put(Long memberId) {
        int random = ThreadLocalRandom.current().nextInt(100000, 1000000);
        String certifyNum = String.valueOf(random);
        certifyNumMap.put(memberId, certifyNum);
        log.info("save certify number id = {}, certifyNum = {}", memberId, certifyNum);
        return certifyNum;
    }

    public boolean compare(Long memberId, String certifyNum) {
        String findCertifyNum = certifyNumMap.get(memberId);
        log.info("compare certify number saved = {}, requested = {}", findCertifyNum, certifyNum);
        return findCertifyNum != null && findCertifyNum.equals(certifyNum);
    }

    public void delete(Long memberId) {
        certifyNumMap.remove(memberId);
        log.info("delete certify number id = {}", memberId);
    }

    @Scheduled(fixedRate = 7 * 60 * 1000)
    public void removeExpiredCertifyNumbers() {
        long currentTime = System.currentTimeMillis();
        certifyNumMap.entrySet().removeIf(entry -> currentTime - entry.getKey() > 7 * 60 * 1000);
        log.info("delete certify number by timeout");
    }
}