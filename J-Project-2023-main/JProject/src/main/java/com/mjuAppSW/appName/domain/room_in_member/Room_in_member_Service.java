package com.mjuAppSW.appName.domain.room_in_member;

import com.mjuAppSW.appName.domain.member.Member;
import com.mjuAppSW.appName.domain.room.Room;
import com.mjuAppSW.appName.domain.room_in_member.dto.RoomListRequest;
import com.mjuAppSW.appName.domain.room_in_member.dto.RoomListResponse;
import com.mjuAppSW.appName.domain.room_in_member.dto.VoteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class Room_in_member_Service {

    @Autowired
    private Room_in_member_Repository room_in_member_repository;

    public boolean findByRoomIdAndMemberId(Room room, Member member){
        Optional<Room_in_member> room_in_member = Optional.ofNullable(room_in_member_repository.findByRoomIdAndMemberId(room, member));
        if(room_in_member.isPresent()){
            char status = room_in_member.get().getExpired();
            if(status == '0'){
                return true;
            }else{return false;}
        }else{
            return true;
        }
    }

    public List<RoomListResponse> getRoomList(Member member, char expired){
       List<Room_in_member> memberList = room_in_member_repository.findByAllMember(member);
       List<RoomListResponse> roomListResponseList = new ArrayList<>();
        if(memberList == null || memberList.isEmpty()){
            return null;
        }else{
            for(Room_in_member rim : memberList){
                List<Room_in_member> list = room_in_member_repository.findByAllRoom(rim.getRoomId());
                for(Room_in_member rim1 : list) {
                    if (rim1.getMemberId().getId() != member.getId()) {
                        RoomListResponse rlr = room_in_member_repository.findByMemberIdAndExpired(rim1.getMemberId().getId(), expired);
                        roomListResponseList.add(rlr);
                    }
                }
            }
        }
       return roomListResponseList;
    }

    public void createRoom(Room room ,Member member){
        Room_in_member room_in_member = Room_in_member.createRoomItem(room, member, '1', '1');
        room_in_member_repository.save(room_in_member);
    }

    public VoteResponse saveVoteResult(Room room, Member member, char result){
        room_in_member_repository.saveVote(room, member, result);
        List<Room_in_member> getRoomInMember = room_in_member_repository.findAllRoom(room);
        for(Room_in_member room_in_member : getRoomInMember){
            if(room_in_member.getMemberId().getId() != member.getId()){
                return new VoteResponse(room_in_member.getRoomId(), room_in_member.getMemberId(), room_in_member.getResult());
            }
        }
        return null;
    }

    public boolean checkRoomIdAndMemberId(Room room, Member member){
        Optional<Room_in_member> check = Optional.ofNullable(room_in_member_repository.findByRoomIdAndMemberId(room, member));
        if(check.isPresent()){
            return true;
        }else{
            return false;
        }
    }

    public int checkVoteResult(Room room, Member member){
        Optional<Room_in_member> check = Optional.ofNullable(room_in_member_repository.findByVote(room, member));
        if(check.isPresent()){
            if(check.get().getResult() == '0'){
                return 0; // 찬성
            }else if(check.get().getResult() == '1'){
                return 1; // 반대
            }else{
                return 2; // 투표 안함
            }
        }else{
            return 3;
        }
    }

    public void updateExpired(Room room, Member member, char expired){
        room_in_member_repository.updateExpired(room, member, expired);
    }
}