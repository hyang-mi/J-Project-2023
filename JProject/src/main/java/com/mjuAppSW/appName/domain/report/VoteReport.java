package com.mjuAppSW.appName.domain.report;

import com.mjuAppSW.appName.domain.vote.Vote;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoteReport {

    @Id @GeneratedValue
    @Column(name = "Report_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Category_id", nullable = false)
    private Vote vote;

    private String content;

    @Column(nullable = false)
    private LocalDateTime date;
}
