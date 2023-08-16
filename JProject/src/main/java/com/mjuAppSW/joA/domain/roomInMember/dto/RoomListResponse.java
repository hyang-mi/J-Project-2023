package com.mjuAppSW.joA.domain.roomInMember.dto;

import com.mjuAppSW.joA.domain.room.Room;
public interface RoomListResponse {
    Room getRoom();
    String getName();
    String getUrlCode();
    String getContent();
}
