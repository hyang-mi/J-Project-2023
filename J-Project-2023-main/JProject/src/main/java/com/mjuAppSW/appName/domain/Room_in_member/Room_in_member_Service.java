package com.mjuAppSW.appName.domain.Room_in_member;

import com.mjuAppSW.appName.domain.Member.Member;
import com.mjuAppSW.appName.domain.Room.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Room_in_member_Service {

    @Autowired
    private Room_in_member_Repository room_in_member_repository;

    public boolean findByRoomIdAndMemberId(Room room, Member member){
//        Long rId = roomId.getRoomId();
//        Long mId = memberId.getId();
        Room_in_member room_in_member = room_in_member_repository.findByRoomIdAndMemberId(room, member);
        if(room_in_member == null){
            return true;
        }else{
            char status = room_in_member.getExpired();
            if(status == '0'){
                return true;
            }else{return false;}
        }
    }

    public Room_in_member_DTO createRoom(Room room ,Member member){
        Room_in_member room_in_member = Room_in_member.createRoomItem(room, member, '1', '1');
        room_in_member_repository.save(room_in_member);

        Room_in_member_DTO room_in_member_dto = Room_in_member_DTO.createRoomDTO(room, member,'1', '1');
        return room_in_member_dto;
    }
}