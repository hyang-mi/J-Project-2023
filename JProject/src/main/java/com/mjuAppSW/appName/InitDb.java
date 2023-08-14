package com.mjuAppSW.appName;

import com.mjuAppSW.appName.domain.college.College;
import com.mjuAppSW.appName.domain.heart.Heart;
import com.mjuAppSW.appName.domain.member.Member;
import com.mjuAppSW.appName.domain.report.ReportCategory;
import com.mjuAppSW.appName.domain.vote.Vote;
import com.mjuAppSW.appName.domain.voteCategory.VoteCategory;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.initCollege();
        initService.initVoteCategory();
        initService.initReportCategory();
        initService.initMember();
        initService.initHeart();
        initService.initVote();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        VoteCategory vc1, vc2, vc3, vc4, vc5, vc6, vc7, vc8;
        ReportCategory rc1, rc2, rc3;
        Member member1, member2, member3, member4, member5;
        Heart heart1, heart2, heart3, heart4, heart5;
        Vote v1, v2, v3, v4, v5, v6, v7, v8, v9, v10;
        College c1, c2;

        public void initCollege() {
            c1 = new College(1L, "명지대학교", "@mju.ac.kr");
            c2 = new College(2L, "애플대학교", "@icloud.com");
            em.persist(c1);
            em.persist(c2);
        }

        public void initVoteCategory() {
            vc1 = new VoteCategory("옷");
            vc2 = new VoteCategory("존잘존예");
            vc3 = new VoteCategory("롤브론즈");
            vc4 = new VoteCategory("과탑");
            vc5 = new VoteCategory("밥");
            vc6 = new VoteCategory("팀플");
            vc7 = new VoteCategory("3대500");
            vc8 = new VoteCategory("카공");

            em.persist(vc1);
            em.persist(vc2);
            em.persist(vc3);
            em.persist(vc4);
            em.persist(vc5);
            em.persist(vc6);
            em.persist(vc7);
            em.persist(vc8);
        }

        public void initReportCategory() {
            rc1 = new ReportCategory("욕설/비방");
            rc2 = new ReportCategory("성희롱");
            rc3 = new ReportCategory("기타");

            em.persist(rc1);
            em.persist(rc2);
            em.persist(rc3);
        }

        public void initMember() {
            member1 = new Member(1L, "최가의", 1L);
            member2 = new Member(2L, "한요한", 2L);
            member3 = new Member(3L, "한태산", 3L);
            member4 = new Member(4L, "홍향미", 4L);
            member5 = new Member(5L, "최종현", 5L);

            em.persist(member1);
            em.persist(member2);
            em.persist(member3);
            em.persist(member4);
            em.persist(member5);
        }

        public void initHeart() {
            heart1 = new Heart(2L, member1, LocalDate.of(2023, 7, 25), false);
            heart2 = new Heart(2L, member1, LocalDate.now(), false);
            heart3 = new Heart(3L, member1, LocalDate.now(), false);
            heart4 = new Heart(4L, member1, LocalDate.now(), false);
            heart5 = new Heart(5L, member1, LocalDate.of(2023, 7, 25), false);

            em.persist(heart1);
            em.persist(heart2);
            em.persist(heart3);
            em.persist(heart4);
            em.persist(heart5);

        }

        public void initVote() {
            v1 = new Vote(2L, member1, vc1, LocalDate.of(2023, 7, 27), "나 누구게");
            v2 = new Vote(3L, member1, vc2, LocalDate.of(2023, 7, 27), "누나 안녕하세여");
            v3 = new Vote(4L, member1, vc3, LocalDate.of(2023, 7, 27), "캡스톤 같이 들으시죠");
            v4 = new Vote(5L, member1, vc4, LocalDate.of(2023, 7, 27), "가 의 님");
            v5 = new Vote(2L, member1, vc5, LocalDate.of(2023, 7, 27), "ㅎ2");
            v6 = new Vote(3L, member1, vc1, LocalDate.now(), "킥킷킥깃");
            v7 = new Vote(4L, member1, vc1, LocalDate.now(), "저 버리고 경정 전공으로 가지 마여");
            v8 = new Vote(5L, member1, vc2, LocalDate.now(), "웹프 에이스");
            v9 = new Vote(2L, member1, vc2, LocalDate.now(), "나 좋아한다고?");
            v10 = new Vote(3L, member1, vc3, LocalDate.now(), null);

            em.persist(v1);
            em.persist(v2);
            em.persist(v3);
            em.persist(v4);
            em.persist(v5);
            em.persist(v6);
            em.persist(v7);
            em.persist(v8);
            em.persist(v9);
            em.persist(v10);
        }
    }
}
