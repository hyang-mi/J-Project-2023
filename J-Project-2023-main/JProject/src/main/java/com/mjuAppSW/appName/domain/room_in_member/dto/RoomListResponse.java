package com.mjuAppSW.appName.domain.room_in_member.dto;

import lombok.Data;

@Data
public class RoomListResponse {
    private long roomId;
    private String name;
    private String imagePath;
    // 최근 메시지 추가
}
