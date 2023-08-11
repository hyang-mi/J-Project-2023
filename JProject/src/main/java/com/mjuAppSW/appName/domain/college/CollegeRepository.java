package com.mjuAppSW.appName.domain.college;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CollegeRepository extends JpaRepository<College, Long> {

    @Query("SELECT c.domain FROM College c WHERE c.id = :id")
    public String findDomainById(@Param("id") Long id);
}
