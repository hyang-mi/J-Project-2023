package com.mjuAppSW.appName.domain.heart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long>  {

    @Query("SELECT COUNT(h) FROM Heart h WHERE h.member.id = :id AND h.date = :today")
    int findTodayHeartsById(@Param("today") LocalDate today, @Param("id") Long id);

    @Query("SELECT COUNT(h) FROM Heart h WHERE h.member.id = :id")
    int findTotalHeartsById(@Param("id") Long id);

    @Query("SELECT h FROM Heart h WHERE h.giveId = :givdId AND h.member.id = :takeId AND h.date = :today")
    Optional<Heart> findEqualHeartByIdAndDate(@Param("today") LocalDate today, @Param("giveId") Long givdId, @Param("takeId") Long takeId);
}
