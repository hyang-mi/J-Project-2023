package com.mjuAppSW.joA.domain.member;

import com.mjuAppSW.joA.domain.college.College;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByloginId(String loginId);

    @Query("SELECT m FROM Member m WHERE m.uEmail = :uEmail AND m.college = :college")
    Optional<Member> findByuEmailAndcollege(@Param("uEmail") String uEmail, @Param("college") College college);
}
