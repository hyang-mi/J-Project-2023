package com.mjuAppSW.appName.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mjuAppSW.appName.domain.heart.Heart;
import com.mjuAppSW.appName.domain.vote.Vote;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue
    @Column(name = "Member_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long kId;

    private String uEmail;

    @Column(length = 15)
    private String introduce;

    private String imagePath; // 사진 경로

    @Column(length = 6)
    private String certifyNum;

    public Member(String name, Long kId) {
        this.name = name;
        this.kId = kId;
    }
}
