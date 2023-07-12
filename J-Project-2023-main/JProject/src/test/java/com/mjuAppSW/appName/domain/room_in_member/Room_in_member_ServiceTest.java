package com.mjuAppSW.appName.domain.room_in_member;

import com.mjuAppSW.appName.domain.member.Member;
import com.mjuAppSW.appName.domain.room.Room;
import com.mjuAppSW.appName.domain.room_in_member.Room_in_member_Repository;
import com.mjuAppSW.appName.domain.room_in_member.Room_in_member_Service;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class Room_in_member_ServiceTest {

    @Autowired
    private Room_in_member_Service room_in_member_service;
    @Autowired
    private Room_in_member_Repository room_in_member_repository;
    @Test
    @Transactional
    @Rollback(false)
    public void testFindByRoomIdAndMemberId() {
        // 테스트에 사용할 Room과 Member 객체 생성
        Room room = new Room();
        room.setRoomId(1L);
        // Room 객체의 다른 속성들을 설정

        Member member = new Member();
        member.setId(1L);
        // Member 객체의 다른 속성들을 설정

        // 테스트 실행
        boolean result = room_in_member_service.findByRoomIdAndMemberId(room, member);

        // 결과 검증
        assertFalse(result);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void createRoom() {
        Room room = new Room();
        room.setRoomId(1L);

        Member member = new Member();
        member.setId(1L);

        Room_in_member room_in_member = new Room_in_member(room, member, '1', '1');

        room_in_member_repository.save(room_in_member);

//        Room_in_member_DTO result = room_in_member_service.createRoom(room, member);
//
//        assertEquals(room.getRoomId(), result.getRoomId());
//        assertEquals(member.getId(), result.getMemberId());

    }
}
