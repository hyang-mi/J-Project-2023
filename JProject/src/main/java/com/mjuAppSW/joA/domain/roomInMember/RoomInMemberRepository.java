package com.mjuAppSW.joA.domain.roomInMember;

import com.mjuAppSW.joA.domain.member.Member;
import com.mjuAppSW.joA.domain.room.Room;
import com.mjuAppSW.joA.domain.roomInMember.dto.RoomListResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface RoomInMemberRepository extends JpaRepository<RoomInMember, Long> {
    @Query("SELECT rim FROM RoomInMember rim WHERE rim.room = :room AND rim.member = :member")
    RoomInMember findByRoomAndMember(@Param("room") Room room, @Param("member") Member member);

    @Query("SELECT rim FROM RoomInMember rim WHERE rim.member = :member")
    List<RoomInMember> findByAllMember(@Param("member") Member member);

    @Query("SELECT rim FROM RoomInMember rim WHERE rim.room = :room")
    List<RoomInMember> findByAllRoom(@Param("room") Room room);

//    @Query("SELECT r.room AS room, m.name AS name, m.imagePath AS imagePath FROM RoomInMember r " +
//        "JOIN r.member m WHERE r.member = :member AND r.room = :room AND r.expired = :expired")
//    RoomListResponse findByMemberIdAndExpired(@Param("member") Member member, @Param("room") Room room, @Param("expired") String expired);

//    @Query("SELECT rim.room AS room, m.name AS name, m.urlCode AS urlCode, mes.content AS content FROM RoomInMember rim " +
//            "LEFT JOIN Member m ON rim.member.id = m.id LEFT JOIN Room r ON rim.room.roomId = r.roomId " +
//            "LEFT JOIN Message mes ON rim.member = mes.member WHERE rim.member = :member AND rim.room = :room AND rim.expired = :expired")
//    RoomListResponse findByMemberIdAndExpired(@Param("member") Member member, @Param("room") Room room, @Param("expired") String expired);

//    @Query("SELECT rim.room AS room, m.name AS name, m.urlCode AS urlCode, mes.content AS content " +
//            "FROM RoomInMember rim " +
//            "LEFT JOIN Member m ON rim.member.id = m.id " +
//            "LEFT JOIN Room r ON rim.room.roomId = r.roomId " +
//            "LEFT JOIN Message mes ON rim.member = mes.member " +
//            "WHERE rim.member = :member AND rim.room = :room AND rim.expired = :expired " +
//            "AND (mes.content IS NULL OR mes.content IS NOT NULL) " +
//            "AND mes.time = (SELECT MAX(mes2.time) FROM Message mes2 WHERE mes2.member = rim.member)" )
//    RoomListResponse findByMemberIdAndExpired(@Param("member") Member member,
//                                              @Param("room") Room room,
//                                              @Param("expired") String expired);

    @Query("SELECT rim.room AS room, m.name AS name, m.urlCode AS urlCode, mes.content AS content " +
            "FROM RoomInMember rim " +
            "LEFT JOIN Member m ON rim.member.id = m.id " +
            "LEFT JOIN Room r ON rim.room.roomId = r.roomId " +
            "LEFT JOIN Message mes ON rim.member = mes.member " +
            "WHERE rim.member = :member AND rim.room = :room AND rim.expired = :expired " +
            "AND (mes.content IS NULL OR mes.content IS NOT NULL OR NOT EXISTS (SELECT 1 FROM Message mes2 WHERE mes2.member = rim.member)) " +
            "AND (mes.time IS NULL OR mes.time = (SELECT MAX(mes2.time) FROM Message mes2 WHERE mes2.member = rim.member))" )
    RoomListResponse findByMemberIdAndExpired(@Param("member") Member member,
                                              @Param("room") Room room,
                                              @Param("expired") String expired);










//    @Query("SELECT rim.room AS room, m.name AS name, m.urlCode AS urlCode, mes.content AS content " +
//            "FROM RoomInMember rim " +
//            "LEFT JOIN Member m ON rim.member.id = m.id " +
//            "LEFT JOIN Room r ON rim.room.roomId = r.roomId " +
//            "LEFT JOIN Message mes ON rim.member = mes.member " +
//            "WHERE rim.member = :member AND rim.room = :room AND rim.expired = :expired " +
//            "AND mes.time = (SELECT MAX(mes2.time) FROM Message mes2 WHERE mes2.member = rim.member)" +
//            "ORDER BY mes.time DESC")
//    RoomListResponse findByMemberIdAndExpired(@Param("member") Member member,
//                                                      @Param("room") Room room,
//                                                      @Param("expired") String expired);


    @Modifying
    @Transactional
    @Query("UPDATE RoomInMember rim set rim.result = :result  WHERE rim.room = :room AND rim.member = :member")
    void saveVote(@Param("room") Room room, @Param("member") Member member, @Param("result") String result);

    @Query("SELECT rim FROM RoomInMember rim Where rim.room = :room")
    List<RoomInMember> findAllRoom(@Param("room") Room room);

    @Query("SELECT rim.room, rim.member, rim.result FROM RoomInMember rim Where rim.room = :room and rim.member = :member")
    RoomInMember findByVote(@Param("room") Room room, @Param("member") Member member);

    @Modifying
    @Transactional
    @Query("UPDATE RoomInMember rim set rim.expired = :expired WHERE rim.room = :room and rim.member = :member")
    void updateExpired(@Param("room") Room room, @Param("member") Member member, @Param("expired") String expired);

    @Modifying
    @Transactional
    @Query("UPDATE RoomInMember rim set rim.entryTime = :date WHERE rim.room = :room and rim.member = :member")
    void updateEntryTime(@Param("room") Room room, @Param("member") Member member, @Param("date") LocalDateTime date);

    @Modifying
    @Transactional
    @Query("UPDATE RoomInMember rim set rim.exitTime = :date WHERE rim.room = :room and rim.member = :member")
    void updateExitTime(@Param("room") Room room, @Param("member") Member member, @Param("date") LocalDateTime date);

}
