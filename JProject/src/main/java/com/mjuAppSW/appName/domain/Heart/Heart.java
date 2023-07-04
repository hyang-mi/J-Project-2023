package com.mju19.appName.domain.Heart;

import com.mju19.appName.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Heart {

    @Id @GeneratedValue
    @Column(name = "Heart_id")
    private long id;

    @Column(nullable = false)
    private long Give_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Take_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private LocalDateTime Date;

    @Column(nullable = false)
    private boolean Named; // char 1?
}
