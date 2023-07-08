package com.mjuAppSW.appName.domain.Room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    public RoomDTO createRoom(){
        Room room = Room.createRoomItem(new Date(), '1');
        roomRepository.save(room);

        RoomDTO roomDTO = RoomDTO.createRoomDTO(room.getRoomId(), room.getDate(), room.getStatus());
        return roomDTO;
    }
}