package com.mjuAppSW.appName.domain.Room_in_member;

import com.mjuAppSW.appName.domain.Member.Member;
import com.mjuAppSW.appName.domain.Room.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Room_in_member")
public class Room_in_member_ApiController {
    @Autowired
    private Room_in_member_Service room_in_member_service;

    @PostMapping("/createRoom_in_member")
    public ResponseEntity<Room_in_member_DTO> createRoom(@RequestBody Room_in_member_VO room_in_member_vo){
        Member member = room_in_member_vo.getMember();
        Room room = room_in_member_vo.getRoom();

        // 1. 이미 생성되어 있는 방인지 확인 <RoomId, MemberId, Expired == '0'> -> 생성 가능
        boolean checkRoomExpired = room_in_member_service.findByRoomIdAndMemberId(room, member);
        if(!checkRoomExpired){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        // Room_in_member 생성
        Room_in_member_DTO room_in_member_dto = room_in_member_service.createRoom(room, member);
        return ResponseEntity.ok(room_in_member_dto);
    }
}