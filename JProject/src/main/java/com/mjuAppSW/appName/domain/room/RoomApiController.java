package com.mjuAppSW.appName.domain.room;

import com.mjuAppSW.appName.domain.room.dto.RoomCheckRequest;
import com.mjuAppSW.appName.domain.room.dto.RoomUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class RoomApiController {

    private RoomService roomService;

    @Autowired
    public RoomApiController(RoomService romRoomService){
        this.roomService = romRoomService;
    }

    @PostMapping("/create/room") // 방 생성
    public HttpStatus createRoom(){
        log.info("createRoom");
        roomService.createRoom();
        return HttpStatus.OK;
    }

    @PostMapping("/check/createdTime") // 방의 생성시간이 24시간이 넘었는지
    public ResponseEntity<String> checkCreatedRoom(@RequestBody RoomCheckRequest roomCheckRequest){
        log.info("checkCreatedRoom");
        log.info("roomId : " + roomCheckRequest.getRoomId());
        int checkCreatedAt = roomService.checkCreateAtRoom(roomCheckRequest.getRoomId());
        if(checkCreatedAt == 0){
            return new ResponseEntity<>("over 24hours", HttpStatus.OK); // 24시간이 넘었을 때
        }else if(checkCreatedAt == 1){
            return new ResponseEntity<>("not over 24hours", HttpStatus.NOT_FOUND); // 24시간이 넘지 않았을 때
        }else{
            return new ResponseEntity<>("roomId is wrong", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/update/createdAt/status") // 투표가 완료되었을 때, 방 상태 없데이트
    public HttpStatus updateRoom(@RequestBody RoomUpdateRequest roomUpdateRequest){
        log.info("updateRoom");
        log.info("roomId : " + roomUpdateRequest.getRoomId());
        log.info("status : " + roomUpdateRequest.getStatus());
        boolean checkRoomId = roomService.checkRoomId(roomUpdateRequest.getRoomId());
        if(!checkRoomId) {
            return HttpStatus.BAD_REQUEST;
        }
        roomService.updateRoom(roomUpdateRequest.getRoomId(), roomUpdateRequest.getStatus());
        return HttpStatus.OK;
    }
}