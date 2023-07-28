package com.mjuAppSW.appName.domain.room_in_member.dto;

import com.mjuAppSW.appName.domain.room.Room;
public interface RoomListResponse {
//    private final Room roomId;
//    private final String name;
//    private final String imagePath;
    Room getRoom();
    String getName();
    String getImagePath();
    // 최근 메시지 추가
}
