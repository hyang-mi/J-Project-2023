package com.mjuAppSW.joA.domain.college;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CollegeRepository extends JpaRepository<College, Long> {

}
