package com.mjuAppSW.joA.domain.college;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MCollege {

    @Id
    @Column(name = "College_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String domain;

//    @Column(nullable = false)
//    private Double topLeftLat;
//
//    @Column(nullable = false)
//    private Double topLeftLong;
//
//    @Column(nullable = false)
//    private Double topRightLat;
//
//    @Column(nullable = false)
//    private Double topRightLong;
//
//    @Column(nullable = false)
//    private Double bottomLeftLat;
//
//    @Column(nullable = false)
//    private Double bottomLeftLong;
//
//    @Column(nullable = false)
//    private Double bottomRightLat;
//
//    @Column(nullable = false)
//    private Double bottomRightLong;

    public MCollege(Long id, String name, String domain) {
        this.id = id;
        this.name = name;
        this.domain = domain;
    }
}
