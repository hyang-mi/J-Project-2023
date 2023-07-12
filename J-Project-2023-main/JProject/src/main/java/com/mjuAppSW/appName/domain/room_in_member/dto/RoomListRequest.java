package com.mjuAppSW.appName.domain.room_in_member.dto;

import com.mjuAppSW.appName.domain.member.Member;
import lombok.Data;

@Data
public class RoomListRequest {
    private Member member;
}
