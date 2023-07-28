package com.mjuAppSW.appName.domain.member;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import com.mjuAppSW.appName.domain.room_in_member.Room_in_member;

@Entity
@Getter @Setter // Setter 지양
@NoArgsConstructor
public class Member {

    @Id @GeneratedValue
    @Column(nullable = false)
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

    @OneToMany(mappedBy = "memberId")
    private List<Room_in_member> roomInMember = new ArrayList<>();

    public Member(long memberId){
        this.id = memberId;
    }
}
