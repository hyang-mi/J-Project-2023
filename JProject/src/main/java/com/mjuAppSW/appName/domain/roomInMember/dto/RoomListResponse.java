package com.mjuAppSW.appName.domain.roomInMember.dto;

import com.mjuAppSW.appName.domain.room.Room;
public interface RoomListResponse {
    Room getRoom();
    String getName();
    String getImagePath();
    String getContent();
}
