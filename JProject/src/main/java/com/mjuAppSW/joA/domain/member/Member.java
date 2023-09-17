package com.mjuAppSW.joA.domain.member;

import com.mjuAppSW.joA.domain.college.MCollege;
import com.mjuAppSW.joA.domain.roomInMember.RoomInMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Column(name = "Login_id", nullable = false)
    private String loginId;

    private String password;

    @Column(name = "U_email")
    private String uEmail;

    @Column(length = 15)
    private String bio;

    @Column(name = "Url_code")
    private String urlCode;

    @Column(name = "Basic_profile")
    private Boolean basicProfile;

    private Boolean withdrawal;

    @ManyToOne
    @JoinColumn(name = "College_id")
    private MCollege college;

    @OneToMany(mappedBy = "member")
    private List<RoomInMember> roomInMember = new ArrayList<>();

    @Builder
    public Member(Long id, String name, String loginId, String password, String uEmail, MCollege college) {
        this.id = id;
        this.name = name;
        this.loginId = loginId;
        this.password = password;
        this.uEmail = uEmail;
        this.college = college;
        this.basicProfile = true;
        this.withdrawal = false;
        this.bio = "";
        this.urlCode = "";
    }

    public void changeName(String name) { this.name = name; }

    public void changeUrlCode(String urlCode) {
        this.urlCode = urlCode;
    }

    public void changeBio(String bio) { this.bio = bio; }

    public void changePassword(String password) { this.password = password; }

    public void changeBasicProfileStatus(boolean basicProfile) {
        this.basicProfile = basicProfile;
    }

    public void changeWithdrawalStatus(boolean withdrawal) {
        this.withdrawal = withdrawal;
    }
}
