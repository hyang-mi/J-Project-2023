package com.mjuAppSW.joA.domain.room;

import com.mjuAppSW.joA.domain.room.dto.RoomCheckRequest;
import com.mjuAppSW.joA.domain.room.dto.RoomResponse;
import com.mjuAppSW.joA.domain.room.dto.RoomUpdateRequest;
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
    public ResponseEntity<RoomResponse> createRoom(){
        log.info("createRoom");
        RoomResponse roomResponse = roomService.createRoom();
        log.info("createRoom Return : roomId = {}", roomResponse.getRoomId());
        return ResponseEntity.ok(roomResponse);
    }

    @PostMapping("/check/createdTime") // 방의 생성시간이 24시간이 넘었는지
    public HttpStatus checkCreatedRoom(@RequestBody RoomCheckRequest roomCheckRequest){
        log.info("checkCreatedTime : roomId = {} ", roomCheckRequest.getRoomId());
        int checkCreatedAt = roomService.checkCreateAtRoom(roomCheckRequest.getRoomId());
        if(checkCreatedAt == 0){
            log.info("checkCreatedTime Return : OK, over 24hours");
            return HttpStatus.OK;
        }else if(checkCreatedAt == 1){
            log.info("checkCreatedTime Return : NOT_FOUND, not over 24hours");
            return HttpStatus.NOT_FOUND;
        }else{
            log.warn("checkCreatedTime Return : BAD_REQUEST, roomId's not correct");
            log.warn("checkCreatedTime : roomId = {}", roomCheckRequest.getRoomId());
            return HttpStatus.BAD_REQUEST;
        }
    }

    @PostMapping("/update/createdAt/status") // 투표가 완료되었을 때, 방 상태 없데이트
    public HttpStatus updateRoom(@RequestBody RoomUpdateRequest roomUpdateRequest){
        log.info("updateCreatedAtStatus : roomId = {}, status = {}", roomUpdateRequest.getRoomId(), roomUpdateRequest.getStatus());
        boolean checkRoomId = roomService.checkRoomId(roomUpdateRequest.getRoomId());
        if(!checkRoomId) {
            log.warn("updateCreatedAtStatus Return : BAD_REQUEST, roomId's not correct");
            log.warn("updateCreatedAtStatus : roomId = {}, status = {}", roomUpdateRequest.getRoomId(), roomUpdateRequest.getStatus());
            return HttpStatus.BAD_REQUEST;
        }
        roomService.updateRoom(roomUpdateRequest.getRoomId(), roomUpdateRequest.getStatus());
        log.info("updateCreatedAtStatus Return : OK, update complete");
        return HttpStatus.OK;
    }
}