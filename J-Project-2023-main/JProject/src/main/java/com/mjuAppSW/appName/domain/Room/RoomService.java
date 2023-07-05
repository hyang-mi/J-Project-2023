package com.mjuAppSW.appName.domain.Room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RoomService {
    private RoomRepository roomRepository;

    public RoomDTO createRoom(){
        Room room = Room.createRoomItem(new Date(), '1');
        roomRepository.save(room);

        RoomDTO roomDTO = RoomDTO.createRoomDTO(room.getRoom_id(), room.getDate(), room.getStatus());
        return roomDTO;
    }
}