package com.mjuAppSW.joA.domain.room;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Room r set r.date = :date, r.status = :status Where r.roomId = :roomId")
    void updateCreatedAtAndStatus(@Param("roomId") Long roomId, @Param("date") LocalDateTime date, @Param("status") String status);

    @Query("SELECT r FROM Room r Where r.roomId = :roomId")
    Room findByDate(@Param("roomId") Long roomId);
}
