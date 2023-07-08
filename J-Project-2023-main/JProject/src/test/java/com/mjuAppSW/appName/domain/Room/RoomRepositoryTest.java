package com.mjuAppSW.appName.domain.Room;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RoomRepositoryTest {
    @Autowired
    private RoomRepository roomRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void save(){
        Date date = new Date();
        char status = '1';
        Room room = new Room(date, status);

        roomRepository.save(room);
    }
}