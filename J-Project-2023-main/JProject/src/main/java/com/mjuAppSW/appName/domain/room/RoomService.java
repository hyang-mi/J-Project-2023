package com.mjuAppSW.appName.domain.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    public void createRoom(){
        Room room = Room.createRoomItem(new Date(), '1');
        roomRepository.save(room);
    }
}