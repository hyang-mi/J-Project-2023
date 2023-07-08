package com.mjuAppSW.appName.domain.Member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import com.mjuAppSW.appName.domain.Room_in_member.Room_in_member;

@Entity
@Getter @Setter // Setter 지양
@NoArgsConstructor
public class Member {

    @Id @GeneratedValue
    @Column(nullable = false)
    private long id;

    @Column(nullable = false)
    private String Name;

    @Column(nullable = false)
    private String K_email;

    private String U_email;

    @Column(length = 15)
    private String Introduce;

    @Lob
    private Blob Picture;

    @OneToMany(mappedBy = "memberId")
    private List<Room_in_member> roomInMember = new ArrayList<>();
}
