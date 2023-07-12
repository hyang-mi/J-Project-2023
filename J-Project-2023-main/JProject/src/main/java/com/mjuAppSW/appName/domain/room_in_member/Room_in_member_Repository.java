package com.mjuAppSW.appName.domain.room_in_member;

import com.mjuAppSW.appName.domain.member.Member;
import com.mjuAppSW.appName.domain.room.Room;
import com.mjuAppSW.appName.domain.room_in_member.dto.RoomListResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Room_in_member_Repository extends JpaRepository<Room_in_member, Long> {
    Room_in_member findByRoomIdAndMemberId(Room roomId, Member memberId);

    @Query() // join Room_in_member, member를 조인해서 name, image같이 가져오기
    List<RoomListResponse> findByAllMember(@Param("member") Member member);
}
