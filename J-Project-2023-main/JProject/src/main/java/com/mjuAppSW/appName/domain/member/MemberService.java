package com.mjuAppSW.appName.domain.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;
    public boolean createMember() {
        Member member = new Member();
        member.setName("jonghyen");
        member.setKId(1L);

        memberRepository.save(member);
        return true;
    }
}
