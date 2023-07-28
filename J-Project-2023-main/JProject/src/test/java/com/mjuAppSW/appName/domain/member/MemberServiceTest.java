package com.mjuAppSW.appName.domain.member;

import com.mjuAppSW.appName.domain.room.Room;
import com.mjuAppSW.appName.domain.room.RoomRepository;
import com.mjuAppSW.appName.domain.room_in_member.Room_in_member;
import com.mjuAppSW.appName.domain.room_in_member.Room_in_member_Repository;
import com.mjuAppSW.appName.domain.room_in_member.Room_in_member_Service;
import com.mjuAppSW.appName.domain.room_in_member.dto.RoomListResponse;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private Room_in_member_Service room_in_member_service;

    @Autowired
    private Room_in_member_Repository room_in_member_repository;

    @BeforeEach
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

    @BeforeEach
    public void save() {
        Date date = new Date();
        char status = '1';
        Room room = new Room(date, status);

        roomRepository.save(room);
    }

//    @Test
//    @Transactional
//    @Rollback(false)
//    void getRoomList() {
//        List<RoomListResponse> memberList = room_in_member_repository.findByAllMember(1);
//        System.out.print("hi" + memberList);
//    }

}