package com.mjuAppSW.appName.domain.Member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;

@Entity
@Getter @Setter // Setter 지양
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue
    @Column(name = "Member_id")
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
}
