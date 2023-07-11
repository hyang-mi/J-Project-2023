package com.mjuAppSW.appName.domain.vote;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    @Query("SELECT vc.name FROM Vote v JOIN v.voteCategory vc WHERE v.member.id = :id GROUP BY vc.name ORDER BY COUNT(vc.name) DESC")
    List<String> findVoteCategoryById(@Param("id") Long id, Pageable pageable);
}
