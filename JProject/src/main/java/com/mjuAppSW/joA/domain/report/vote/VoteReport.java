package com.mjuAppSW.joA.domain.report.vote;

import com.mjuAppSW.joA.domain.report.ReportCategory;
import com.mjuAppSW.joA.domain.vote.Vote;
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
    @JoinColumn(name = "Vote_id", nullable = false)
    private Vote vote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Category_id", nullable = false)
    private ReportCategory reportCategory;

    private String content;

    @Column(nullable = false)
    private LocalDateTime date;

    public VoteReport(Vote vote, ReportCategory reportCategory, String content, LocalDateTime date) {
        this.vote = vote;
        this.reportCategory = reportCategory;
        this.content = content;
        this.date = date;
    }
}
