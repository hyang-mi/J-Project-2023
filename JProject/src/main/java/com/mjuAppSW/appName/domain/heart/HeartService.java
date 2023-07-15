package com.mjuAppSW.appName.domain.heart;

import com.mjuAppSW.appName.domain.heart.dto.HeartRequest;
import com.mjuAppSW.appName.domain.heart.dto.HeartResponse;
import com.mjuAppSW.appName.domain.member.Member;
import com.mjuAppSW.appName.domain.member.MemberRepository;
import com.mjuAppSW.appName.picture.RedisUploader;
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
    private final RedisUploader redisUploader;

    public HeartResponse sendHeart(HeartRequest request) {
        Long giveId = request.getGiveId();
        Long takeId = request.getTakeId();
        boolean named = request.getNamed();

        Optional<Member> findGiveMember = memberRepository.findById(giveId);
        Optional<Member> findTakeMember = memberRepository.findById(takeId);
        if (findGiveMember.isEmpty() || findTakeMember.isEmpty())
            return new HeartResponse(2); // id가 존재하지 않음, 유효하지 않은 접근

        Optional<Heart> equalHeart = heartRepository.findEqualHeartByIdAndDate(LocalDate.now(), giveId, takeId);
        if (equalHeart.isPresent())
            return new HeartResponse(1); // 이미 하트를 누른 상태

        Heart heart = new Heart(giveId, findTakeMember.get(), LocalDate.now(), named);
        heartRepository.save(heart);
        Optional<Heart> opponentHeart = heartRepository.findEqualHeartByIdAndDate(LocalDate.now(), takeId, giveId);
        Member giveMember = findGiveMember.get();
        Member takeMember = findTakeMember.get();
        String giveMemberName = giveMember.getName();

        if(opponentHeart.isPresent()) {
            String giveImage = null;
            String takeImage = null;
            if(!giveMember.getBasicProfile())
                giveImage = redisUploader.bringPicture(giveMember.getId());
            if(!takeMember.getBasicProfile())
                takeImage = redisUploader.bringPicture(takeMember.getId());
            return new HeartResponse(0,true, giveMemberName, giveImage, takeImage);
        }
        else {
            if (named)
                return new HeartResponse(0,false, giveMemberName, null, null);
            else
                return new HeartResponse(0,false, null, null, null);
        }
    }
}
