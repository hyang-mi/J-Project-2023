package com.mjuAppSW.joA.geography;

import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GeoRepository extends JpaRepository<Location, Long> {

    @Query(value = "SELECT l.member_id " +
            "FROM location l " +
            "WHERE ST_3DDWithin(:point, l.member_point, 2) " +
            "AND l.college_id = :collegeId " +
            "AND ST_DWithin(:point, l.member_point, 100) " +
            "AND l.member_id <> :memberId " +
            "LIMIT 50", nativeQuery = true)
    List<Long> findNearIds(@Param("memberId") Long memberId, @Param("point") Point point, @Param("collegeId") Long CollegeId);

    @Override
    @Query("SELECT l FROM Location l WHERE l.id = :memberId")
    Optional<Location> findById(@Param("memberId") Long memberId);

}
