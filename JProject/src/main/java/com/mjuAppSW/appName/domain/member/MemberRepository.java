package com.mjuAppSW.appName.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findBykId(Long kId);

    Optional<Member> findByuEmail(String uEmail);

//    @Modifying(clearAutomatically = true)
//    @Query("UPDATE Member m SET m.uEmail = :uEmail WHERE m.id = :id")
//    void saveUEmailById(@Param("uEmail") String uEmail, @Param("id") Long id);

//    @Modifying(clearAutomatically = true)
//    @Query("UPDATE Member m SET m.basicProfile = :basicProfile WHERE m.id = :id")
//    void saveBasicProfileById(@Param(("basicProfile")) Boolean basicProfile, @Param("id") Long id);

//    @Modifying(clearAutomatically = true)
//    @Query("UPDATE Member m SET m.bio = :introduce WHERE m.id = :id")
//    void saveIntroduceById(@Param("introduce") String introduce, @Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Member m SET m.withdrawal = true WHERE m.id = :id")
    void setWithdrawalById(@Param("id") Long id);

}
