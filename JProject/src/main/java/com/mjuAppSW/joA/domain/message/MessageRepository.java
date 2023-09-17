package com.mjuAppSW.joA.domain.message;

import com.mjuAppSW.joA.domain.member.Member;
import com.mjuAppSW.joA.domain.room.Room;
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

    @Query("SELECT m FROM Message m WHERE m.room = :room AND m.isChecked = '1' AND m.member <> :member")
    List<Message> findMessage(@Param("room") Room room, @Param("member") Member member);


    @Query("SELECT COUNT(m) FROM Message m WHERE m.room = :room AND m.member = :member AND m.isChecked = '1'")
    Integer countUnCheckedMessage(@Param("room") Room room, @Param("member") Member member);

    @Modifying
    @Transactional
    @Query("DELETE FROM Message m WHERE m.room = :room")
    void deleteByRoom(@Param("room") Room room);

    @Modifying
    @Transactional
    @Query("UPDATE Message m SET m.isChecked = '0' WHERE m In :messages")
    void updateIsChecked(@Param("messages") List<Message> messages);
}
