package com.mjuAppSW.appName.domain.member;

import com.mjuAppSW.appName.domain.heart.HeartRepository;
import com.mjuAppSW.appName.domain.member.dto.*;
import com.mjuAppSW.appName.domain.vote.VoteRepository;
import com.mjuAppSW.appName.picture.RedisUploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final HeartRepository heartRepository;
    private final VoteRepository voteRepository;
    private final JavaMailSender javaMailSender;
    private final RedisUploader redisUploader;

    public LoginResponse login(LoginRequest request) {
        ResponseEntity<KakaoUserResponse> kakaoResponse = getUserInfo(request.getAccessToken());

        if (kakaoResponse.getStatusCode() == HttpStatus.OK) {
            KakaoUserResponse kakaoResponseBody = kakaoResponse.getBody();
            Member findMember = memberRepository.findBykId(kakaoResponseBody.getKId());

            if(findMember != null) {
                String uEmail = findMember.getUEmail();
                if(uEmail != null)
                    return new LoginResponse(findMember.getId(), findMember.getName(), uEmail);
                return new LoginResponse(findMember.getId(), findMember.getName());
            }
            else {
                Member newMember = new Member(
                        kakaoResponseBody.getKakaoAccount().getName(), kakaoResponseBody.getKId(), true);
                memberRepository.save(newMember);
                Member savedMember = memberRepository.findBykId(kakaoResponseBody.getKId());
                return new LoginResponse(savedMember.getId(), savedMember.getName());
            }
        }
        else {
            log.info("사용자 정보 조회가 올바르지 않음");
            return null;
        }
    }

    public ResponseEntity<KakaoUserResponse> getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 카카오 사용자 정보를 조회하기 위한 엔드포인트 URL
        String kakaoUserInfoUrl = "https://kapi.kakao.com/v2/user/me";

        try {
            return restTemplate.exchange(kakaoUserInfoUrl, HttpMethod.GET, entity, KakaoUserResponse.class);
        }
        catch(HttpClientErrorException e) {
            log.info("access token이 유효하지 않음");
            return ResponseEntity.badRequest().build();
        }
    }

    public UMailResponse sendCertifyNum(UMailRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getUEmail() + "@mju.ac.kr");
        message.setSubject("[앱이름] 인증번호를 확인하세요.");

        int certifyNum = ThreadLocalRandom.current().nextInt(100000, 1000000);
        memberRepository.saveCertifyNumById(String.valueOf(certifyNum), request.getId());
        Optional<Member> member = memberRepository.findById(request.getId());

        if(member.isEmpty()) return null;

        message.setText(String.valueOf("인증번호는 [" + certifyNum + "] 입니다."));
        javaMailSender.send(message);
        return new ModelMapper().map(member, UMailResponse.class);
    }

    public boolean authCertifyNum(UNumRequest request) {
        Optional<Member> findMember = memberRepository.findById(request.getId());

        if(findMember.isEmpty()) return false;

        String savedCertifyNum = findMember.get().getCertifyNum();
        if (request.getCertifyNum().equals(savedCertifyNum)) {
            memberRepository.saveUEmailById(request.getUEmail(), request.getId());
            return true;
        }
        return false;
    }

    public SetResponse set(MemberRequest request) {
        Optional<Member> findMember = memberRepository.findById(request.getId());

        if (findMember.isEmpty()) return null; // id 값이 유효하지 않은 경우

        Member member = findMember.get();
        SetResponse response = new SetResponse();
        response.setName(member.getName());

        if(member.getBasicProfile()) return response; // 기본 프로필 사진인 경우

        response.setBase64Picture(redisUploader.bringPicture(member.getId()));
        return response;
    }

    public void withdrawal(MemberRequest request) {
        memberRepository.deleteById(request.getId());
    }

    public MyPageResponse sendMyPage(MemberRequest request) {
        Optional<Member> findMember = memberRepository.findById(request.getId());

        if (findMember.isEmpty()) return null;

        Member member = findMember.get();
        MyPageResponse response = new MyPageResponse(member.getName(), member.getIntroduce());

        if(!member.getBasicProfile())
            response.setBase64Picture(redisUploader.bringPicture(member.getId()));

        int todayHeart = heartRepository.findTodayHeartsById(LocalDate.now(), member.getId());
        response.setTodayHeart(todayHeart);

        int totalHeart = heartRepository.findTotalHeartsById(member.getId());
        response.setTotalHeart(totalHeart);

        Pageable pageable = PageRequest.of(0, 3);
        response.setVoteTop3(voteRepository.findVoteCategoryById(member.getId(), pageable));
        return response;
    }

    public boolean transPicture(ImageRequest request) {
        Optional<Member> findMember = memberRepository.findById(request.getId());
        if(findMember.isEmpty()) return false;
        Member member = findMember.get();

        redisUploader.updatePicture(request.getId(), request.getBase64Picture());
        if(member.getBasicProfile())
            memberRepository.saveBasicProfileById(false, member.getId());
        return true;
    }

    public boolean deletePicture(MemberRequest request) {
        Optional<Member> findMember = memberRepository.findById(request.getId());
        if(findMember.isEmpty()) return false; // id 값이 유효하지 않은 경우

        memberRepository.saveBasicProfileById(true, request.getId());
        return true;
    }

    public boolean transIntroduce(IntroduceRequest request) {
        Optional<Member> findMember = memberRepository.findById(request.getId());
        if(findMember.isEmpty()) return false; // id 값이 유효하지 않은 경우

        memberRepository.saveIntroduceById(request.getIntroduce(), request.getId());
        return true;
    }

    public ProfileResponse getOtherProfile(MemberRequest request) {
        Optional<Member> findMember = memberRepository.findById(request.getId());
        if(findMember.isEmpty()) return null;

        Member member = findMember.get();
        String base64Picture = null;
        if(!member.getBasicProfile()) base64Picture = redisUploader.bringPicture(request.getId());

        return new ProfileResponse(member.getName(), base64Picture, member.getIntroduce());
    }
}
