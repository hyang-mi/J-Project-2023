package com.mjuAppSW.appName.domain.member;

import com.mjuAppSW.appName.domain.heart.HeartRepository;
import com.mjuAppSW.appName.domain.member.dto.*;
import com.mjuAppSW.appName.domain.vote.VoteRepository;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Base64;
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

    public LoginResponse login(String accessToken) {
        ResponseEntity<KakaoUserResponse> kakaoResponse = getUserInfo(accessToken);

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
                Member newMember = new Member(kakaoResponseBody.getKakaoAccount().getName(), kakaoResponseBody.getKId());
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

    public UMailResponse sendCertifyNum(String UId, Long id) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(UId + "@mju.ac.kr");
        message.setSubject("[앱이름] 인증번호를 확인하세요.");

        int certifyNum = ThreadLocalRandom.current().nextInt(100000, 1000000);
        memberRepository.saveCertifyNumById(String.valueOf(certifyNum), id);
        Optional<Member> member = memberRepository.findById(id);

        if(member.isPresent()) {
            message.setText(String.valueOf("인증번호는 [" + certifyNum + "] 입니다."));
            javaMailSender.send(message);
            return new ModelMapper().map(member, UMailResponse.class);
        }
        else {
            return null;
        }
    }

    public boolean authCertifyNum(String certifyNum, String uEmail, Long id) {
        Optional<Member> findMember = memberRepository.findById(id);
        if (findMember.isPresent()) {
            String savedCertifyNum = findMember.get().getCertifyNum();
            if (certifyNum.equals(savedCertifyNum)) {
                memberRepository.saveUEmailById(uEmail, id);
                return true;
            }
            return false;
        }
        return false;
    }

    public SetResponse set(Long id) {
        Optional<Member> findMember = memberRepository.findById(id);
        if(findMember.isPresent()) {
            Member member = findMember.get();
            SetResponse response = new SetResponse();
            response.setName(member.getName());

            String base64Image = findImage(member);
            response.setBase64Image(base64Image);
            return response;
        }
        else {
            return null; // id 값이 유효하지 않은 경우
        }
    }

    public boolean withdrawal(Long id) {
        memberRepository.deleteById(id);
        if (!memberRepository.existsById(id)) {
            return true;
        }
        return false;
    }

    public MyPageResponse sendMyPage(Long id) {
        Optional<Member> findMember = memberRepository.findById(id);
        if (findMember.isPresent()) {
            Member member = findMember.get();
            MyPageResponse response = new MyPageResponse(member.getName(), member.getIntroduce());

            String base64Image = findImage(member);
            response.setBase64Image(base64Image);

            int todayHeart = heartRepository.findTodayHeartsById(LocalDate.now(), member.getId());
            response.setTodayHeart(todayHeart);

            int totalHeart = heartRepository.findTotalHeartsById(member.getId());
            response.setTotalHeart(totalHeart);

            Pageable pageable = PageRequest.of(0, 3);
            response.setVoteTop3(voteRepository.findVoteCategoryById(member.getId(), pageable));
            return response;
        }
        else {
            return null; // id 값이 유효하지 않은 경우
        }
    }

    public boolean transImage(Long id, String encodedImage) {
        Optional<Member> findMember = memberRepository.findById(id);
        if (findMember.isPresent()) {
            return saveImage(findMember.get(), encodedImage);
        }
        else {
            return false; // id 값이 유효하지 않은 경우
        }
    }

    public boolean transIntroduce(Long id, String introduce) {
        Optional<Member> findMember = memberRepository.findById(id);
        if (findMember.isPresent()) {
            memberRepository.saveIntroduceById(introduce, id);
            return true;
        }
        else {
            return false; // id 값이 유효하지 않은 경우
        }
    }

    private String findImage(Member member) {
        String imagePath = member.getImagePath();
        if(imagePath == null) // 저장소에 이미지 경로 미리 있느냐 / 없느냐에 따라 구현 달라질 듯
            return null;
        return encodeImage(imagePath);
    }

    private String encodeImage(String imagePath) {
        String filePath = "storagePath" + imagePath; // 파일 저장소 경로
        File imageFile = new File(filePath);
        try {
            // imageFile이 null이라면?
            byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            return base64Image;
        } catch (IOException e) {
            log.info("프로필 사진 경로가 유효하지 않음");
            return null;
        }
    }

    private boolean saveImage(Member member, String encodedImage){
        byte[] decodedBytes = Base64.getDecoder().decode(encodedImage);
        String imagePath = member.getImagePath();

        if (imagePath == null) { // 임시, 주소 클라우드에서 자동 설정할 것, picture 저장하는 로직 바꿔야 함
            imagePath = String.valueOf(member.getId());
            memberRepository.saveImagePathById(imagePath, member.getId());
        }
        try {
            FileOutputStream fos = new FileOutputStream("storagePath" + imagePath); // 파일 저장소 경로
            fos.write(decodedBytes);
            return true;
        }
        catch (IOException e) {
            log.info("프로필 사진 경로가 유효하지 않음");
            return false;
        }
    }

    public ProfileResponse getOtherProfile(Long id) {
        Optional<Member> findMember = memberRepository.findById(id);
        if (findMember.isPresent()) {
            Member member = findMember.get();
            String base64Image = findImage(member);
            return new ProfileResponse(member.getName(), base64Image, member.getIntroduce());
        }
        else {
            return null;
        }
    }
}
