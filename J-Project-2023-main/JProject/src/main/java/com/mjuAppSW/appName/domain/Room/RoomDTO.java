package com.mjuAppSW.appName.domain.Room;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RoomDTO {
    private int Room_id;
    private java.util.Date Date;
    private char Status;

    public static RoomDTO createRoomDTO(int room_id, Date date, char status){
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setRoom_id(room_id);
        roomDTO.setDate(date);
        roomDTO.setStatus(status);
        return roomDTO;
    }
}
