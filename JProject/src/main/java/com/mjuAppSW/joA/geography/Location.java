package com.mjuAppSW.joA.geography;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @Column(name = "College_id")
    private Long collegeId; // 학교 아이디와 동일하게

    public Location(Long id, Long collegeId) {
        this.id = id;
        this.collegeId = collegeId;
    }

    public Location(Long id, Long collegeId, Point point) {
        this.id = id;
        this.collegeId = collegeId;
        this.point = point;
    }
}
