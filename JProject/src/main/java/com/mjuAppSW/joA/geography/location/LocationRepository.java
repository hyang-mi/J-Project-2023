package com.mjuAppSW.joA.geography.location;

import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query(value = "SELECT l.member_id " +
            "FROM location l " +
            "WHERE ST_DWithin(l.member_point, :point, 0.000899) " +
            "AND ABS(ST_Z(l.Member_point) - ST_Z(:point)) <= 2 " +
            "AND l.member_id <> :memberId " +
            "AND l.college_id = :collegeId " +
            "AND l.is_contained = true " +
            "ORDER BY ST_Distance(l.Member_point, :point) " +
            "LIMIT 50", nativeQuery = true)
    List<Long> findNearIds(@Param("memberId") Long memberId, @Param("point") Point point, @Param("collegeId") Long collegeId);

    @Override
    @Query("SELECT l FROM Location l WHERE l.id = :memberId")
    Optional<Location> findById(@Param("memberId") Long memberId);
    
}
