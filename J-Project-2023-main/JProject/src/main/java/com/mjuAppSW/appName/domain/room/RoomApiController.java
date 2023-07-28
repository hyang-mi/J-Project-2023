package com.mjuAppSW.appName.domain.room;

import com.mjuAppSW.appName.domain.room.dto.RoomCheckRequest;
import com.mjuAppSW.appName.domain.room.dto.RoomUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Room")
public class RoomApiController {
    @Autowired
    private RoomService roomService;

    @PostMapping("/createRoom") // 방 생성
    public HttpStatus createRoom(){
        roomService.createRoom();
        return HttpStatus.OK;
    }
    @PostMapping("/checkCreatedAtRoom") // 방의 생성시간이 24시간이 넘었는지
    public HttpStatus checkCreatedRoom(@RequestBody RoomCheckRequest roomCheckRequest){
        long roomId = roomCheckRequest.getRoomId();
        boolean checkRoomId = roomService.checkRoomId(roomId);
        if(!checkRoomId){
            return HttpStatus.NOT_FOUND;
        }
        int checkCreatedAt = roomService.checkCreateAtRoom(roomId);
        if(checkCreatedAt == 0){
            return HttpStatus.OK; // 24시간이 넘었을 때
        }else if(checkCreatedAt == 1){
            return HttpStatus.OK; // 24시간이 넘지 않았을 때
        }else{
            return HttpStatus.NOT_FOUND; // roomId가 잘못되었을 때
        }
    }

    @PostMapping("/updateCreatedAtAndStatusRoom") // 투표가 완료되었을 때, 방 상태 없데이트
    public HttpStatus updateRoom(@RequestBody RoomUpdateRequest roomUpdateRequest){
        boolean checkRoomId = roomService.checkRoomId(roomUpdateRequest.getRoomId());
        if(!checkRoomId) {
            return HttpStatus.NOT_FOUND;
        }
        roomService.updateRoom(roomUpdateRequest.getRoomId(), roomUpdateRequest.getDate(), roomUpdateRequest.getStatus());
        return HttpStatus.OK;
    }
}