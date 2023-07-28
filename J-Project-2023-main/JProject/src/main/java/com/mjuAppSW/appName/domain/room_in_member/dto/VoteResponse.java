package com.mjuAppSW.appName.domain.room_in_member.dto;

import com.mjuAppSW.appName.domain.member.Member;
import com.mjuAppSW.appName.domain.room.Room;
import lombok.Data;

@Data
public class VoteResponse {
    private Room room;
    private Member member;
    private char result;

    public VoteResponse(Room roomId, Member memberId, char result) {
        this.room = roomId;
        this.member = memberId;
        this.result = result;
    }
}
