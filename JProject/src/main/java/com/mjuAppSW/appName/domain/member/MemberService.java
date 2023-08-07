package com.mjuAppSW.appName.domain.member;

import com.mjuAppSW.appName.domain.heart.HeartRepository;
import com.mjuAppSW.appName.domain.member.dto.*;
import com.mjuAppSW.appName.domain.vote.VoteRepository;
import com.mjuAppSW.appName.geography.GeoRepository;
import com.mjuAppSW.appName.storage.RedisStorage;
import com.mjuAppSW.appName.storage.S3Uploader;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final HeartRepository heartRepository;
    private final VoteRepository voteRepository;
    private final GeoRepository geoRepository;
    private final JavaMailSender javaMailSender;
    private final RedisStorage redisStorage;
    private final S3Uploader s3Uploader;

    @Transactional
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
                Member newMember = new Member(getRandomId(), kakaoResponseBody.getKakaoAccount().getName(), kakaoResponseBody.getKId());
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

    private static long getRandomId() {
        long min = 1000000000L;
        long max = 9999999999L;
        Random random = new Random();
        long range = max - min + 1;
        long randomId = (long) (random.nextDouble() * range) + min;
        return randomId;
    }

    public ResponseEntity<KakaoUserResponse> getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 카카오 사용자 정보를 조회하기 위한 엔드포인트 URL
        String kakaoUserInfoUrl = "https://kapi.kakao.com/v2/user/me";

        try {
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.exchange(kakaoUserInfoUrl, HttpMethod.GET, entity, KakaoUserResponse.class);
        }
        catch(HttpClientErrorException e) {
            log.info("access token이 유효하지 않음");
            return ResponseEntity.badRequest().build();
        }
    }

    @Transactional
    public MemberResponse sendCertifyNum(UMailRequest request) {
        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            if (member.getUEmail() != null && member.getUEmail() == request.getUEmail())
                return new MemberResponse(1);
        }

        Optional<Member> member = memberRepository.findById(request.getId());
        if(member.isEmpty())
            return new MemberResponse(2);

        memberRepository.saveUEmailById(request.getUEmail(), request.getId());
        int certifyNum = ThreadLocalRandom.current().nextInt(100000, 1000000);
        redisStorage.setCertifyNum(request.getId(), certifyNum);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getUEmail() + "@mju.ac.kr");
        message.setSubject("[앱이름] 인증번호를 확인하세요.");
        message.setText("인증번호는 [" + certifyNum + "] 입니다.");

        javaMailSender.send(message);
        return new MemberResponse(0);
    }

    @Transactional
    public Boolean authCertifyNum(UNumRequest request) {
        Optional<Member> findMember = memberRepository.findById(request.getId());
        if(findMember.isEmpty()) return false;
        if (request.getCertifyNum().equals(redisStorage.getCertifyNum(request.getId()))) return true;
        return false;
    }

    public SetResponse set(Long id) {
        Member member = memberRepository.findById(id).orElse(null);
        if(member == null) return null; // id 값이 유효하지 않은 경우

        String base64Picture = null;
        if(!member.getBasicProfile())
            base64Picture = s3Uploader.getPicture(String.valueOf(member.getId()));

        return SetResponse.builder().name(member.getName())
                                    .base64Picture(base64Picture)
                                    .build();
    }

    public MyPageResponse sendMyPage(Long id) {
        Member member = memberRepository.findById(id).orElse(null);
        if(member == null) return null; // id 값이 유효하지 않은 경우

        int todayHeart = heartRepository.countTodayHeartsById(LocalDate.now(), member.getId());
        int totalHeart = heartRepository.countTotalHeartsById(member.getId());
        Pageable pageable = PageRequest.of(0, 3);
        List<String> voteTop3 = voteRepository.findVoteCategoryById(member.getId(), pageable);

        String base64Picture = null;
        if(!member.getBasicProfile()) base64Picture = s3Uploader.getPicture(String.valueOf(member.getId()));

        return MyPageResponse.builder().name(member.getName())
                                        .bio(member.getBio())
                                        .base64Picture(base64Picture)
                                        .todayHeart(todayHeart)
                                        .totalHeart(totalHeart)
                                        .voteTop3(voteTop3).build();
    }

    @Transactional
    public MemberResponse transPicture(PictureRequest request) {
        Member member = memberRepository.findById(request.getId()).orElse(null);
        if(member == null) return new MemberResponse(2); // id 값이 유효하지 않은 경우

        boolean isUploaded = s3Uploader.putPicture(String.valueOf(member.getId()), request.getBase64Picture());
        if(isUploaded) {
            if (member.getBasicProfile()) {
                memberRepository.saveBasicProfileById(false, member.getId());
                return new MemberResponse(0);
            }
        }
        return new MemberResponse(1); // 업로드 안 된 경우
    }

    @Transactional
    public boolean deletePicture(MemberRequest request) {
        Optional<Member> findMember = memberRepository.findById(request.getId());
        if(findMember.isEmpty()) return false; // id 값이 유효하지 않은 경우

        memberRepository.saveBasicProfileById(true, request.getId());
        return true;
    }

    @Transactional
    public boolean transBio(BioRequest request) {
        Optional<Member> findMember = memberRepository.findById(request.getId());
        if(findMember.isEmpty()) return false; // id 값이 유효하지 않은 경우

        memberRepository.saveIntroduceById(request.getBio(), request.getId());
        return true;
    }

    @Transactional
    public void withdrawal(MemberRequest request) {
        memberRepository.saveWithdrawalById(request.getId());
        geoRepository.deleteById(request.getId());
    }
}
