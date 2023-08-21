package com.mjuAppSW.joA.domain.heart;

import com.mjuAppSW.joA.domain.heart.dto.HeartRequest;
import com.mjuAppSW.joA.domain.heart.dto.HeartResponse;
import com.mjuAppSW.joA.domain.member.Member;
import com.mjuAppSW.joA.domain.member.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class HeartService {

    private final HeartRepository heartRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public HeartResponse sendHeart(HeartRequest request) {
        Long giveId = request.getGiveId();
        Long takeId = request.getTakeId();
        boolean named = request.getNamed();

        Member giveMember = findByMemberId(giveId);
        Member takeMember = findByMemberId(takeId);
        if (giveMember == null || takeMember == null)
            return new HeartResponse(2); // id가 존재하지 않음, 유효하지 않은 접근

        Heart equalHeart = findEqualHeartByIdAndDate(giveId, takeId);
        if (equalHeart != null)
            return new HeartResponse(1); // 이미 하트를 누른 상태

        Heart heart = Heart.builder().giveId(giveId)
                                .member(takeMember)
                                .date(LocalDate.now())
                                .named(named).build();
        heartRepository.save(heart);
        Heart opponentHeart = findEqualHeartByIdAndDate(takeId, giveId);
        String giveMemberName = giveMember.getName();
        String giveUrlCode = null;
        if(!giveMember.getBasicProfile())
            giveUrlCode = giveMember.getUrlCode();

        if(opponentHeart != null) {
            String takeMemberName = takeMember.getName();
            String takeUrlCode = null;
            if(!takeMember.getBasicProfile())
                takeUrlCode = takeMember.getUrlCode();
            return HeartResponse.builder().status(0)
                                    .isMatched(true)
                                    .giveName(giveMemberName)
                                    .takeName(takeMemberName)
                                    .giveUrlCode(giveUrlCode)
                                    .takeUrlCode(takeUrlCode)
                                    .build();
        }
        if (named)
            return HeartResponse.builder().status(0)
                                    .isMatched(false)
                                    .giveName(giveMemberName)
                                    .giveUrlCode(giveUrlCode)
                                    .build();
        else
            return HeartResponse.builder().status(0)
                                        .isMatched(false).build();
    }

    private Member findByMemberId(Long id) {
        Member member = memberRepository.findById(id).orElse(null);
        if(member.getWithdrawal() == true) return null;
        return member;
    }

    private Heart findEqualHeartByIdAndDate(Long giveId, Long takeId) {
        return heartRepository.findEqualHeartByIdAndDate(LocalDate.now(), giveId, takeId).orElse(null);
    }
}
