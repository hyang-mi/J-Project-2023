package com.mjuAppSW.appName.domain.roomInMember.dto;

import lombok.Data;

@Data
public class RoomDTO {
    private long roomId;
    private String name;
    private String imagePath;
    private String content;

    public RoomDTO(long roomId, String name, String imagePath, String content) {
        this.roomId = roomId;
        this.name = name;
        this.imagePath = imagePath;
        this.content = content;
    }
}
