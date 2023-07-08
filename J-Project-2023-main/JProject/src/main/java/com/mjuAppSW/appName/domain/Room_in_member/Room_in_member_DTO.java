package com.mjuAppSW.appName.domain.Room_in_member;

import com.mjuAppSW.appName.domain.Member.Member;
import com.mjuAppSW.appName.domain.Room.Room;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Room_in_member_DTO {
    private Member memberId;
    private Room roomId;
    private char expired;
    private char result;
    private Date entryTime;
    private Date exitTime;

    public static Room_in_member_DTO createRoomDTO(Room room, Member member, char expired, char result){
        Room_in_member_DTO room_in_member_dto = new Room_in_member_DTO();
        room_in_member_dto.setRoomId(room);
        room_in_member_dto.setMemberId(member);
        room_in_member_dto.setExpired(expired);
        room_in_member_dto.setResult(result);
        return room_in_member_dto;
    }
}
