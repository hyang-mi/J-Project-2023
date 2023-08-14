package com.mjuAppSW.appName.domain.room.dto;

import lombok.Data;

@Data
public class RoomResponse {
    private Long roomId;

    public RoomResponse(Long roomId){
        this.roomId = roomId;
    }
}
