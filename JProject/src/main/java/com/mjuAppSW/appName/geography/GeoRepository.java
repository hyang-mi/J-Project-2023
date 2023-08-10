package com.mjuAppSW.appName.domain.geography;

import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GeoRepository extends JpaRepository<Location, Long> {

    @Query(value = "SELECT l.Member_id " +
            "FROM location l " +
            "WHERE ST_3DDWithin(:point, l.Member_point, 2) " +
            "AND l.College_id = :collegeId " +
            "AND ST_DWithin(:point, l.Member_point, 100) " +
            "AND l.Member_id <> :memberId " +
            "LIMIT 50", nativeQuery = true)
    List<Long> findNearById(@Param("memberId") Long memberId, @Param("point") Point point, @Param("collegeId") Long CollegeId);

}
