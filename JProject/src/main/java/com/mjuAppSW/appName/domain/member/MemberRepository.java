package com.mjuAppSW.appName.domain.member;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findBykId(Long kId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Member m SET m.certifyNum = :certifyNum WHERE m.id = :id")
    void saveCertifyNumById(@Param("certifyNum") String certifyNum, @Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Member m SET m.uEmail = :uEmail WHERE m.id = :id")
    void saveUEmailById(@Param("uEmail") String uEmail, @Param("id") Long id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Member m SET m.basicProfile = :basicProfile WHERE m.id = :id")
    void saveBasicProfileById(@Param(("basicProfile")) Boolean basicProfile, @Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Member m SET m.introduce = :introduce WHERE m.id = :id")
    void saveIntroduceById(@Param("introduce") String introduce, @Param("id") Long id);

}
