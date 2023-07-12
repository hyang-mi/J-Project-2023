package com.mjuAppSW.appName.domain.room;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RoomDTO {
    private long roomId;
    private Date date;
    private char Status;
}
