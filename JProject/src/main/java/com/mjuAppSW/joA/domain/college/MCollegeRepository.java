package com.mjuAppSW.joA.domain.college;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MCollegeRepository extends JpaRepository<MCollege, Long> {

    Optional<MCollege> findBydomain(String domain);
}
