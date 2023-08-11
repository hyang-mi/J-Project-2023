package com.mjuAppSW.appName.domain.message;

import com.mjuAppSW.appName.domain.member.Member;
import com.mjuAppSW.appName.domain.room.Room;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m Where m.room = :room")
    List<Message> findByRoom(@Param("room") Room room);

//    @Modifying
//    @Transactional
//    @Query("UPDATE Message m set m.isChecked = :isChecked Where m.room = :room AND m.member = :member")
//    void updateIsChecked(@Param("room") Room room, @Param("member") Member member, @Param("isChecked") String isChecked);
}
