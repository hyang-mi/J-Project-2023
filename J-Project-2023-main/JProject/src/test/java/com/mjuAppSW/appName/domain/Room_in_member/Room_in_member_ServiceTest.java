package com.mjuAppSW.appName.domain.Room_in_member;

import com.mjuAppSW.appName.domain.Member.Member;
import com.mjuAppSW.appName.domain.Room.Room;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class Room_in_member_ServiceTest {

    @Autowired
    private Room_in_member_Service room_in_member_service;
    @Autowired
    private Room_in_member_Repository room_in_member_repository;

    @Test
    @Transactional
    @Rollback(false)
    public void createRoom(){
        Room room = new Room();
        room.setRoomId(1L);

        Member member = new Member();
        member.setId(1L);

        Room_in_member room_in_member = new Room_in_member();
        room_in_member.setRoomId(room);
        room_in_member.setMemberId(member);
        room_in_member.setResult('1');
        room_in_member.setExpired('1');

        room_in_member_repository.save(room_in_member);
    }
}