package com.mjuAppSW.appName.domain.member;

import com.mjuAppSW.appName.domain.college.College;
import com.mjuAppSW.appName.domain.roomInMember.RoomInMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @Column
    private String urlCode;

    @Column(name = "Basic_profile")
    private Boolean basicProfile;

    private Boolean withdrawal;

    @OneToOne
    @JoinColumn(name = "College_id")
    private College college;

    @OneToMany(mappedBy = "member")
    private List<RoomInMember> roomInMember = new ArrayList<>();


    public Member(Long id, String name, Long kId) {
        this.id = id;
        this.name = name;
        this.kId = kId;
        this.basicProfile = true;
        this.withdrawal = false;
    }

    public void setUEmailAndCollege(String uEmail, College college) {
        this.uEmail = uEmail;
        this.college = college;
    }

    public void changeUrlCode(String urlCode) {
        this.urlCode = urlCode;
    }

    public void changeBio(String bio) {
        this.bio = bio;
    }

    public void changeBasicProfileStatus(boolean basicProfile) {
        this.basicProfile = basicProfile;
    }
}
