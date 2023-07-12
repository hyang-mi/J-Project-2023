package com.mjuAppSW.appName.domain.room_in_member;

import com.mjuAppSW.appName.domain.room_in_member.dto.RoomListRequest;
import com.mjuAppSW.appName.domain.room_in_member.dto.RoomListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Room_in_member")
public class Room_in_member_ApiController {
    @Autowired
    private Room_in_member_Service room_in_member_service;

    @GetMapping("/loadRoom_in_member")
    public ResponseEntity<List<RoomListResponse>> getRoomList(@RequestBody RoomListRequest roomListRequest){
        List<RoomListResponse> response = room_in_member_service.getRoomList(roomListRequest.getMember());
        if(response != null){
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }
}