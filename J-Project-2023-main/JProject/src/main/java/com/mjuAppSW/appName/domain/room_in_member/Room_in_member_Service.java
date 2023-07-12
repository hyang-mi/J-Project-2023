package com.mjuAppSW.appName.domain.room_in_member;

import com.mjuAppSW.appName.domain.member.Member;
import com.mjuAppSW.appName.domain.room.Room;
import com.mjuAppSW.appName.domain.room_in_member.dto.RoomListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Room_in_member_Service {

    @Autowired
    private Room_in_member_Repository room_in_member_repository;

    public boolean findByRoomIdAndMemberId(Room room, Member member){
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

    public List<RoomListResponse> getRoomList(Member member){
       List<RoomListResponse> memberList = room_in_member_repository.findByAllMember(member);
       return memberList;
    }

    public void createRoom(Room room ,Member member){
        Room_in_member room_in_member = Room_in_member.createRoomItem(room, member, '1', '1');
        room_in_member_repository.save(room_in_member);
    }
}