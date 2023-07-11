package com.mjuAppSW.appName.domain.heart;

import com.mjuAppSW.appName.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Heart {

    @Id @GeneratedValue
    @Column(name = "Heart_id")
    private Long id;

    @Column(nullable = false)
    private Long giveId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Take_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private LocalDate date; // LocalDateTime으로 수정해야, 변환 조회 방법 모르겠어서 일단 이렇게

    @Column(nullable = false)
    private Boolean named;

    public Heart(long giveId, Member member, LocalDate date, boolean named) {
        this.giveId = giveId;
        this.member = member;
        this.date = date;
        this.named = named;
    }
}