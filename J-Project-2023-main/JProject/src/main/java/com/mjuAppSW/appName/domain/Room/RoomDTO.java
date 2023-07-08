package com.mjuAppSW.appName.domain.Room;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RoomDTO {
    private long roomId;
    private java.util.Date Date;
    private char Status;

    public static RoomDTO createRoomDTO(long roomId, Date date, char status){
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setRoomId(roomId);
        roomDTO.setDate(date);
        roomDTO.setStatus(status);
        return roomDTO;
    }
}
