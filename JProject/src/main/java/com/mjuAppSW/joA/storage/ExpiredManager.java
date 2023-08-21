package com.mjuAppSW.joA.storage;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class IdManager {

    private final Map<Long, SavedIdAndTime> idMap = new ConcurrentHashMap<>();

    public void add(Long key, SavedIdAndTime value) {
        idMap.put(key, value);
        scheduledCleanUp(key);
    }

    @Async
    public void scheduledCleanUp(Long key) {
        try {
            Thread.sleep(Duration.ofHours(1).toMillis());
            cleanUpExpired(key);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void cleanUpExpired(Long key) {
        SavedIdAndTime value = idMap.get(key);
        LocalDateTime savedTime = value.getSavedTime();
        if (savedTime != null && Duration.between(savedTime, LocalDateTime.now()).toHours() >= 1) {
            idMap.remove(key);
        }
    }

    static class SavedIdAndTime {
        private String loginId;

        private LocalDateTime savedTime;

        public SavedIdAndTime(String loginId, LocalDateTime savedTime) {
            this.loginId = loginId;
            this.savedTime = savedTime;
        }

        public LocalDateTime getSavedTime() {
            return savedTime;
        }
    }
}
