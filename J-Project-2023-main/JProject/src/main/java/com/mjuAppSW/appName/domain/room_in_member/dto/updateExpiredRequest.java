package com.mjuAppSW.appName.domain.room_in_member.dto;

import com.mjuAppSW.appName.domain.member.Member;
import com.mjuAppSW.appName.domain.room.Room;
import lombok.Data;

@Data
public class updateExpiredRequest {
    private Room room;
    private Member member;
    private char expired;
}
