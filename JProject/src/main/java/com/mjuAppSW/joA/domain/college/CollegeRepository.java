package com.mjuAppSW.joA.domain.college;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CollegeRepository extends JpaRepository<College, Long> {

    Optional<College> findBydomain(String domain);
}
