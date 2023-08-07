package com.mjuAppSW.appName.domain.heart;

import com.mjuAppSW.appName.domain.heart.dto.HeartRequest;
import com.mjuAppSW.appName.domain.heart.dto.HeartResponse;
import com.mjuAppSW.appName.domain.member.Member;
import com.mjuAppSW.appName.domain.member.MemberRepository;
import com.mjuAppSW.appName.storage.S3Uploader;
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
    private final S3Uploader s3Uploader;

    @Transactional
    public HeartResponse sendHeart(HeartRequest request) {
        Long giveId = request.getGiveId();
        Long takeId = request.getTakeId();
        boolean named = request.getNamed();

        Member giveMember = memberRepository.findById(giveId).orElse(null);
        Member takeMember = memberRepository.findById(takeId).orElse(null);
        if (giveMember == null || takeMember == null)
            return new HeartResponse(2); // id가 존재하지 않음, 유효하지 않은 접근

        Optional<Heart> equalHeart = heartRepository.findEqualHeartByIdAndDate(LocalDate.now(), giveId, takeId);
        if (equalHeart.isPresent())
            return new HeartResponse(1); // 이미 하트를 누른 상태

        Heart heart = Heart.builder().giveId(giveId)
                                        .member(takeMember)
                                        .date(LocalDate.now())
                                        .named(named).build();
        heartRepository.save(heart);
        Optional<Heart> opponentHeart = heartRepository.findEqualHeartByIdAndDate(LocalDate.now(), takeId, giveId);
        String giveMemberName = giveMember.getName();

        if(opponentHeart.isPresent()) {
            String givePicture = null;
            String takePicture = null;
            if(!giveMember.getBasicProfile())
                givePicture = s3Uploader.getPicture(String.valueOf(giveMember.getId()));
            if(!takeMember.getBasicProfile())
                takePicture = s3Uploader.getPicture(String.valueOf(takeMember.getId()));
            return HeartResponse.builder().status(0)
                                            .isMatched(true)
                                            .giveName(giveMemberName)
                                            .giveBase64Picture(givePicture)
                                            .takeBase64Picture(takePicture).build();

        }
        else {
            if (named)
                return HeartResponse.builder().status(0)
                                                .isMatched(false)
                                                .giveName(giveMemberName)
                                                .giveBase64Picture(null)
                                                .takeBase64Picture(null).build();
            else
                return HeartResponse.builder().status(0)
                                                .isMatched(false)
                                                .giveName(null)
                                                .giveBase64Picture(null)
                                                .takeBase64Picture(null).build();
        }
    }
}
