package com.mjuAppSW.appName.domain.room_in_member;

import com.mjuAppSW.appName.domain.member.Member;
import com.mjuAppSW.appName.domain.room.Room;
import com.mjuAppSW.appName.domain.room_in_member.dto.RoomListResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface Room_in_member_Repository extends JpaRepository<Room_in_member, Long> {
    @Query("SELECT rim FROM Room_in_member rim WHERE rim.roomId = :roomId AND rim.memberId = :memberId")
    Room_in_member findByRoomIdAndMemberId(@Param("roomId") Room roomId, @Param("memberId") Member memberId);

    @Query("SELECT rim FROM Room_in_member rim WHERE rim.memberId = :memberId")
    List<Room_in_member> findByAllMember(@Param("memberId") Member memberId);

    @Query("SELECT rim FROM Room_in_member rim WHERE rim.roomId = :roomId")
    List<Room_in_member> findByAllRoom(@Param("roomId") Room RoomId);

//    @Query("SELECT m.name As name, m.imagePath AS image_Path, rim.roomId AS room_Id FROM Member m join fetch m.roomInMember rim WHERE m.id = :memberId AND rim.expired = :expired")
//    @Query("SELECT m.name As name, m.imagePath AS image_Path, rim.roomId AS room_Id FROM Member m INNER JOIN Room_in_member rim ON m.id = rim.memberId.id WHERE m.id = :memberId AND rim.expired = :expired")
//    @Query("SELECT m.name AS name, m.imagePath AS image_Path, rim.roomId AS room_Id " +
//            "FROM Member m " +
//            "JOIN Room_in_member rim ON m.id = rim.memberId.id " +
//            "WHERE m.id = :memberId AND rim.expired = :expired")
    @Query("SELECT r.roomId AS room, m.name AS name, m.imagePath AS imagePath FROM Room_in_member r " +
        "JOIN r.memberId m WHERE r.memberId.id = :memberId AND r.expired = :expired")
    RoomListResponse findByMemberIdAndExpired(@Param("memberId") Long memberId, @Param("expired") char expired);

    @Modifying
    @Transactional
    @Query("UPDATE Room_in_member rim set rim.result = :result  WHERE rim.roomId = :roomId AND rim.memberId = :memberId")
    void saveVote(@Param("roomId") Room roomId, @Param("memberId") Member memberId, @Param("result") char result);

    @Query("SELECT rim.roomId, rim.memberId, rim.result FROM Room_in_member rim Where rim.roomId = :roomId and rim.memberId = :memberId")
    List<Room_in_member> findAllRoom(@Param("roomId") Room room);

    @Query("SELECT rim.roomId, rim.memberId, rim.result FROM Room_in_member rim Where rim.roomId = :roomId and rim.memberId = :memberId")
    Room_in_member findByVote(@Param("roomId") Room roomId, @Param("memberId") Member memberId);

    @Modifying
    @Transactional
    @Query("UPDATE Room_in_member rim set rim.expired = :expired WHERE rim.roomId = :roomId and rim.memberId = :memberId")
    void updateExpired(@Param("roomId") Room roomId, @Param("memberId") Member memberId, @Param("expired") char expired);
}
