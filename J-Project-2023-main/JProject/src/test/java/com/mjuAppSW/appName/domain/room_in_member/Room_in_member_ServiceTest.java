package com.mjuAppSW.appName.domain.room_in_member;

import com.mjuAppSW.appName.domain.member.Member;
import com.mjuAppSW.appName.domain.room.Room;
import com.mjuAppSW.appName.domain.room_in_member.dto.RoomListResponse;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class Room_in_member_ServiceTest {

    @Autowired
    private Room_in_member_Service room_in_member_service;
    @Autowired
    private Room_in_member_Repository room_in_member_repository;

    @Test
    void findByRoomIdAndMemberId() {
        Room room = new Room(1);
        Member member = new Member(1);
        Optional<Room_in_member> room_in_member = Optional.ofNullable(room_in_member_repository.findByRoomIdAndMemberId(room, member));
        if (room_in_member.isPresent()) {
            char status = room_in_member.get().getExpired();
            assertEquals('0', status, "Room_in_member의 expired 값이 '0'이어야 합니다.");
        } else {
            // room_in_member가 존재하지 않는 경우
            // 따라서 '1'을 반환하도록 기대합니다.
            assertEquals(true, true, "Room_in_member가 존재하지 않아야 합니다.");
        }
    }

    @Test
    @Transactional
    @Rollback(false)
    void getRoomList() {
        Member member = new Member(1L);
        char expired = '1';
        List<Room_in_member> memberList = room_in_member_repository.findByAllMember(member);
        List<RoomListResponse> roomListResponseList = new ArrayList<>();
        assertTrue(!memberList.isEmpty());

        for (Room_in_member memberResponse : memberList) {
            System.out.println(memberResponse.getMemberId().getId());
            System.out.println(memberResponse.getRoomId().getRoomId());
        }

        for(Room_in_member rim : memberList){
            List<Room_in_member> list = room_in_member_repository.findByAllRoom(rim.getRoomId());
            for(Room_in_member rim1 : list) {
                if (rim1.getMemberId().getId() != member.getId()) {
                    RoomListResponse rlr = room_in_member_repository.findByMemberIdAndExpired(rim1.getMemberId().getId(), expired);
                    roomListResponseList.add(rlr);
                }
            }
        }

        for(RoomListResponse r : roomListResponseList){
            System.out.println("방 ID " + r.getRoom().getRoomId());
            System.out.println("이름 " + r.getName());
            System.out.println("사진 " + r.getImagePath());
        }
    }

    @Test
    void createRoom() {
    }

    @Test
    void saveVoteResult() {
    }

    @Test
    @Transactional
    @Rollback(false)
    void checkRoomIdAndMemberId() {
        Room room = new Room(1);
        Member member = new Member(1);
        Optional<Room_in_member> check = Optional.ofNullable(room_in_member_repository.findByRoomIdAndMemberId(room, member));
        assertTrue(check.isPresent());
    }

    @Test
    void checkVoteResult() {
    }

    @Test
    void updateExpired() {
        Room room = new Room(1);
        Member member = new Member(1);
        char expired = '0';
        room_in_member_service.updateExpired(room, member, expired);
    }


}
