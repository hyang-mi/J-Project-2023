package com.mjuAppSW.appName.geography;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location {

    @Id
    @Column(name = "Member_id")
    private Long id; // 멤버 아이디와 동일하게

    @Column(name = "Member_point", columnDefinition = "geometry(PointZ, 4326)")
    private Point point;

    public Location(Long id, Point point){
        this.id = id;
        this.point = point;
    }
}
