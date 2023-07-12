package com.mjuAppSW.appName.domain.room;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.Date;

@SpringBootTest
class RoomRepositoryTest {
    @Autowired
    private RoomRepository roomRepository;

    @Test
    @Transactional
    @Rollback(false)
    @BeforeEach
    public void save(){
        Date date = new Date();
        char status = '1';
        Room room = new Room(date, status);

        roomRepository.save(room);
    }
}