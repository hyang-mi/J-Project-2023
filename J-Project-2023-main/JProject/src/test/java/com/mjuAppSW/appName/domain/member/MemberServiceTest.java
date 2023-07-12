package com.mjuAppSW.appName.domain.member;

import com.mjuAppSW.appName.domain.room.Room;
import com.mjuAppSW.appName.domain.room.RoomRepository;
import com.mjuAppSW.appName.domain.room_in_member.Room_in_member_Repository;
import com.mjuAppSW.appName.domain.room_in_member.Room_in_member_Service;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private Room_in_member_Service room_in_member_service;

    @Test
    @Transactional
    @Rollback(false)
    void createMember() {
        Member member = new Member();
        member.setName("jonghyen");
        member.setKId(1L);

        Member member1 = new Member();
        member1.setName("dongsu");
        member1.setKId(1L);

        memberRepository.save(member);
        memberRepository.save(member1);
    }

    @Test
    @Transactional
    @Rollback(false)
    @BeforeEach
    public void save(){
        Date date = new Date();
        char status = '1';
        Room room = new Room(date, status);

        roomRepository.save(room);
    }

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
}