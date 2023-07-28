package com.mjuAppSW.appName.domain.room.dto;

import lombok.Data;

import java.util.Date;

@Data
public class RoomUpdateRequest {
    private long roomId;
    private Date date;
    private char status;
}
