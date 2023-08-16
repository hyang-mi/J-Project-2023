package com.mjuAppSW.joA.domain.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageReportRepository extends JpaRepository<MessageReport, Long> {

}
