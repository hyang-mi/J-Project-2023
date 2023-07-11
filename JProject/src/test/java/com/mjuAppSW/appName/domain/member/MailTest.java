package com.mjuAppSW.appName.domain.member;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class MailTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void testSendMail() {
        long kId = 1L;
        Member member = new Member("memberA", kId);
        memberRepository.save(member);
        memberService.sendCertifyNum("UEmailA", kId);

        Member findMember = memberRepository.findBykId(kId);

        assertThat(findMember.getName()).isEqualTo("memberA");
        assertThat(findMember.getKId()).isEqualTo(kId);
        assertThat(findMember.getCertifyNum()).isNotNull();
    }

    @Test
    @Transactional
    @Rollback(false)
    public void testAuthMail() {
        long kId = 1L;
        Member member = new Member("memberA", kId);
        memberRepository.save(member);
        memberService.sendCertifyNum("UEmailA", kId);

        Member findMember = memberRepository.findBykId(kId);
        String findCertifyNum = findMember.getCertifyNum();

        boolean isCorrect = memberService.authCertifyNum(findCertifyNum, "UEmailA", kId);
        Member resultMember = memberRepository.findBykId(kId);

        assertThat(isCorrect).isTrue();
        assertThat(resultMember.getUEmail()).isEqualTo("UEmailA");
        assertThat(resultMember.getCertifyNum()).isEqualTo(findCertifyNum);
    }
}
