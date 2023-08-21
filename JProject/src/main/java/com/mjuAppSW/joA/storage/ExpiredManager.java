package com.mjuAppSW.joA.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ExpiredManager {

    private final Map<String, SavedValueAndTime> expireDataMap = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void add(String key, String value, int minute) {
        SavedValueAndTime savedValueAndTime = new SavedValueAndTime(value, LocalDateTime.now());
        expireDataMap.put(key, savedValueAndTime);
        log.info("put to expireDataMap key = {}, value = {}, minute = {}", key, value, minute);
        long seconds = convertToSeconds(minute);
        scheduledCleanUp(key, seconds);
    }

    private long convertToSeconds(int minute) {
        return minute * 60;
    }

    public boolean isExistedKey(String key) {
        return expireDataMap.containsKey(key);
    }

    public boolean isExistedValue(String startWith, String checkValue) {
        return expireDataMap.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(startWith))
                .anyMatch(entry -> entry.getValue().getSavedValue().equals(checkValue));
    }

    public boolean compare(String key, String value) {
        SavedValueAndTime savedValueAndTime = expireDataMap.get(key);
        if(savedValueAndTime != null) {
            if(savedValueAndTime.getSavedValue().equals(value))
                return true;
        }
        return false;
    }

    public String getSavedValue(String key) {
        SavedValueAndTime savedValueAndTime = expireDataMap.get(key);
        return savedValueAndTime.getSavedValue();
    }

    public void changeTime(String key, int minute) {
        String savedValue = expireDataMap.get(key).getSavedValue();
        add(key, savedValue, minute);
    }

    public void delete(String key) {
        expireDataMap.remove(key);
        log.info("remove to expireDataMap key ={}", key);
    }

    private void scheduledCleanUp(String key, long seconds) {
        scheduler.schedule(() -> {
            cleanUpExpired(key, seconds);
        }, seconds, TimeUnit.SECONDS);
    }

    private void cleanUpExpired(String key, long seconds) {
        SavedValueAndTime value = expireDataMap.get(key);
        LocalDateTime savedTime = value.getSavedTime();
        if (savedTime != null && isTimeOver(savedTime, seconds)) {
            expireDataMap.remove(key);
            log.info("clean up to expireDataMap key ={}", key);
        }
    }

    private boolean isTimeOver(LocalDateTime time, long seconds) {
        return Duration.between(time, LocalDateTime.now()).getSeconds() >= seconds;
    }

    @Getter
    static class SavedValueAndTime {
        private String savedValue;
        private LocalDateTime savedTime;

        public SavedValueAndTime(String value, LocalDateTime savedTime) {
            this.savedValue = value;
            this.savedTime = savedTime;
        }
    }
}