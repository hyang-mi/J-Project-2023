package com.mjuAppSW.appName.domain.room_in_member;

import com.mjuAppSW.appName.domain.room_in_member.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Room_in_member")
public class Room_in_member_ApiController {
    @Autowired
    private Room_in_member_Service room_in_member_service;

    @GetMapping("/loadRoom_in_member") // 채팅방 목록
    public ResponseEntity<List<RoomListResponse>> getRoomList(@RequestBody RoomListRequest roomListRequest){
        List<RoomListResponse> response = room_in_member_service.getRoomList(roomListRequest.getMemberId(), roomListRequest.getExpired());
        if(response != null){
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/voteRoom_in_member") // 투표 저장하기
    public ResponseEntity<VoteResponse> saveVoteResult(@RequestBody VoteRequest voteRequest){
        boolean checkRoomIdAndMemberId = room_in_member_service.checkRoomIdAndMemberId(voteRequest.getRoom(), voteRequest.getMember());
        if(!checkRoomIdAndMemberId){
            return ResponseEntity.notFound().build(); // requestBody의 정보가 잘못되었을 때
        }
        VoteResponse response = room_in_member_service.saveVoteResult(voteRequest.getRoom(), voteRequest.getMember(), voteRequest.getResult());
        if(response != null){
            return ResponseEntity.ok(response); // 성공
        }
        return ResponseEntity.badRequest().build(); // 실패
    }

    @PostMapping("/checkVoteResultRoom_in_member") // 두 회원의 투표 결과 확인하기
    public HttpStatus checkVoteResult(@RequestBody CheckVoteRequest checkVoteRequest){
        boolean checkRoomIdAndMemberId = room_in_member_service.checkRoomIdAndMemberId(checkVoteRequest.getRoom(), checkVoteRequest.getMember());
        if(!checkRoomIdAndMemberId){
            return HttpStatus.NOT_FOUND; // requestBody의 정보가 잘못되었을 때
        }
        int getResult = room_in_member_service.checkVoteResult(checkVoteRequest.getRoom(), checkVoteRequest.getMember());
        if(getResult == 0){
            return HttpStatus.OK; // 찬성
        }else if(getResult == 1){
            return HttpStatus.OK; // 반대
        }else if(getResult == 2){
            return HttpStatus.OK; // 투표 안함
        }else{
            return HttpStatus.BAD_REQUEST; // 값이 존재하지 않을때, vote결과전에 값을 확인하기 때문에 처리할 필요는 없음.
        }
    }

    @PostMapping("/updateExpiredRoom_in_member") // 나간 회원의 expired 1 -> 0으로 업데이트하기
    public HttpStatus updateExpired(@RequestBody updateExpiredRequest updateExpiredRequest){
        boolean check = room_in_member_service.checkRoomIdAndMemberId(updateExpiredRequest.getRoom(), updateExpiredRequest.getMember());
        if(!check){
            return HttpStatus.NOT_FOUND;
        }
        room_in_member_service.updateExpired(updateExpiredRequest.getRoom(), updateExpiredRequest.getMember(), updateExpiredRequest.getExpired());
        return HttpStatus.OK;
    }
}