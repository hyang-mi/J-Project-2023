package com.mjuAppSW.appName.domain.room;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Room r set r.date = :date, r.status = :status Where r.roomId = :roomId")
    void updateCreatedAtAndStatus(@Param("roomId") long roomId, @Param("date") Date date, @Param("status") char status);

    @Query("SELECT r.roomId, r.date FROM Room r Where r.roomId = :roomId")
    Room findByDate(@Param("roomId") long roomId);
}
