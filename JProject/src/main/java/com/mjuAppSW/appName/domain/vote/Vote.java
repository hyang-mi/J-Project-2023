package com.mju19.appName.domain.vote;

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
public class Vote {

    @Id @GeneratedValue
    @Column(name = "Vote_id")
    private long id;

    @Column(nullable = false)
    private long Give_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Take_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Category_id", nullable = false)
    private Vote_category vote_category;

    @Column(nullable = false)
    private LocalDateTime Date;

    private String Hint;
}
