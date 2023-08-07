package com.mjuAppSW.appName.domain.member;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @Column(name = "Member_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "K_id", nullable = false)
    private Long kId;

    @Column(name = "U_email")
    private String uEmail;

    @Column(length = 15)
    private String bio;

    @Column(name = "Basic_profile")
    private Boolean basicProfile;

    private Boolean withdrawal;

    public Member(Long id, String name, Long kId) {
        this.id = id;
        this.name = name;
        this.kId = kId;
        this.basicProfile = true;
        this.withdrawal = false;
        this.bio = "";
    }
}
