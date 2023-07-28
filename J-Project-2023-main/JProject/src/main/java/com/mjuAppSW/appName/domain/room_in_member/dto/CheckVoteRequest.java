package com.mjuAppSW.appName.domain.room_in_member.dto;

import com.mjuAppSW.appName.domain.member.Member;
import com.mjuAppSW.appName.domain.room.Room;
import lombok.Data;

import java.util.Vector;

@Data
public class CheckVoteRequest {
    private Room room;
    private Member member;
}
