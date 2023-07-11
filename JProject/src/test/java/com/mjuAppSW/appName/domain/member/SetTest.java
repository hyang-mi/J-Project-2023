package com.mjuAppSW.appName.domain.member;

import com.mjuAppSW.appName.domain.heart.Heart;
import com.mjuAppSW.appName.domain.heart.HeartRepository;
import com.mjuAppSW.appName.domain.member.dto.MyPageResponse;
import com.mjuAppSW.appName.domain.vote.Vote;
import com.mjuAppSW.appName.domain.VoteCategory.VoteCategory;
import com.mjuAppSW.appName.domain.VoteCategory.VoteCategoryRepository;
import com.mjuAppSW.appName.domain.vote.VoteRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class SetTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    HeartRepository heartRepository;
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    VoteCategoryRepository voteCategoryRepository;

    // 설정(이미지) 테스트


    @Test
    @Transactional
    @Rollback(false)
    public void testGetHearts() {
        Member memberA = new Member("memberA", 1L);
        Member memberB = new Member("memberB", 2L);
        Member memberC = new Member("memberC", 3L);
        Member memberD = new Member("memberD", 4L);

        memberRepository.save(memberA);
        memberRepository.save(memberB);
        memberRepository.save(memberC);
        memberRepository.save(memberD);

        LocalDate A = LocalDate.of(2023, 7, 9);
        LocalDate B = LocalDate.of(2023, 7, 10);
        LocalDate C = LocalDate.now();

        Heart heartA = new Heart(10L, 1L, memberD, A, true);
        Heart heartB = new Heart(11L, 2L, memberD, B, true);
        Heart heartC = new Heart(12L, 3L, memberD, C, true);

        heartRepository.save(heartA);
        heartRepository.save(heartB);
        heartRepository.save(heartC);

        MyPageResponse response = memberService.sendMyPage(4L);
        assertThat(response.getTodayHeart()).isEqualTo(2);
        assertThat(response.getTotalHeart()).isEqualTo(3);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void testGetTop3Category() {
        Member memberA = new Member("memberA", 1L);
        Member memberB = new Member("memberB", 2L);
        Member memberC = new Member("memberC", 3L);
        Member memberD = new Member("memberD", 4L);

        memberRepository.save(memberA);
        memberRepository.save(memberB);
        memberRepository.save(memberC);
        memberRepository.save(memberD);

        VoteCategory C1 = new VoteCategory("C1");
        VoteCategory C2 = new VoteCategory("C2");
        VoteCategory C3 = new VoteCategory("C3");
        VoteCategory C4 = new VoteCategory("C4");
        VoteCategory C5 = new VoteCategory("C5");

        voteCategoryRepository.save(C1);
        voteCategoryRepository.save(C2);
        voteCategoryRepository.save(C3);
        voteCategoryRepository.save(C4);
        voteCategoryRepository.save(C5);

        Vote V1 = new Vote(memberA.getId(), memberD, C1,LocalDateTime.now());
        Vote V2 = new Vote(memberA.getId(), memberD, C2,LocalDateTime.now());
        Vote V3 = new Vote(memberA.getId(), memberD, C3,LocalDateTime.now());
        Vote V4 = new Vote(memberA.getId(), memberD, C4,LocalDateTime.now());
        Vote V5 = new Vote(memberA.getId(), memberD, C5,LocalDateTime.now());
        Vote V6 = new Vote(memberA.getId(), memberD, C1,LocalDateTime.now());
        Vote V7 = new Vote(memberA.getId(), memberD, C1,LocalDateTime.now());
        Vote V8 = new Vote(memberA.getId(), memberD, C2,LocalDateTime.now());
        Vote V9 = new Vote(memberA.getId(), memberD, C3,LocalDateTime.now());
        Vote V10 = new Vote(memberA.getId(), memberD, C1,LocalDateTime.now());

        voteRepository.save(V1);
        voteRepository.save(V2);
        voteRepository.save(V3);
        voteRepository.save(V4);
        voteRepository.save(V5);
        voteRepository.save(V6);
        voteRepository.save(V7);
        voteRepository.save(V8);
        voteRepository.save(V9);
        voteRepository.save(V10);

        MyPageResponse response = memberService.sendMyPage(memberD.getId());
        List<String> voteTop3 = response.getVoteTop3();
        assertThat(voteTop3).containsExactly("C1", "C2", "C3");
    }

    // 프로필 사진 변경

    // 한줄 소개 변경
    @Test
    @Transactional
    @Rollback(false)
    public void testTransIntroduce() {
        Member memberA = new Member("memberA", 1L);
        Member memberB = new Member("memberB", 2L);

        memberRepository.save(memberA);
        memberRepository.save(memberB);

        memberService.transIntroduce(memberA.getId(), "HiA");
        memberService.transIntroduce(memberB.getId(), "HiB");

        memberService.transIntroduce(memberA.getId(), "HiAA");

        Member findMemberA = memberRepository.findBykId(memberA.getKId());
        Member findMemberB = memberRepository.findBykId(memberB.getKId());

        assertThat(findMemberA.getIntroduce()).isEqualTo("HiAA");
        assertThat(findMemberB.getIntroduce()).isEqualTo("HiB");
    }
}
