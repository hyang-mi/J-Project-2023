package com.mjuAppSW.appName.domain.member.geography;

import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GeoRepository extends JpaRepository<Location, Long> {

    @Query(value = "SELECT l.id FROM Location l WHERE l.id <> :memberId " +
            "ORDER BY ST_Distance_Sphere(l.point, :point) LIMIT 50", nativeQuery = true)
    List<Long> findNearByIds(@Param("memberId") Long memberId, @Param("point") Point point);
}
