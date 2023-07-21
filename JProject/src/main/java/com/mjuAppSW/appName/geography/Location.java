package com.mjuAppSW.appName.geography;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location {

    @Id
    @Column(name = "Member_id")
    private Long id; // 멤버 아이디와 동일하게

    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point point;

    public Location(Long id, Point point){
        this.id = id;
        this.point = point;
    }
}
