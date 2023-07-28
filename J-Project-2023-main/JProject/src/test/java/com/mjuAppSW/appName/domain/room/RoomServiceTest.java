package com.mjuAppSW.appName.domain.room;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class RoomServiceTest {

    @Autowired
    private RoomRepository roomRepository;

    @Test
    void createRoom() {
    }

    @Test
    void testCheckRoomId() {
    }


    @Test
    void updateRoom() {
    }

    @Test
    @Transactional
    @Rollback(false)
    void checkRoomId() {
        long roomId = 1;
        Optional<Room> findRoomId = roomRepository.findById(roomId);
        assertTrue(findRoomId.isPresent());
    }
}
