package com.mjuAppSW.appName.domain.Room_in_member;

import com.mjuAppSW.appName.domain.Member.Member;
import com.mjuAppSW.appName.domain.Room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Room_in_member_Repository extends JpaRepository<Room_in_member, Long> {
    Room_in_member save(Room_in_member room_in_member);
    Room_in_member findByRoomIdAndMemberId(Room roomId, Member memberId);
}
