package com.mjuAppSW.joA.domain.member;

import com.mjuAppSW.joA.domain.college.College;
import com.mjuAppSW.joA.domain.college.CollegeRepository;
import com.mjuAppSW.joA.geography.Location;
import com.mjuAppSW.joA.domain.heart.HeartRepository;
import com.mjuAppSW.joA.domain.member.dto.*;
import com.mjuAppSW.joA.domain.vote.VoteRepository;
import com.mjuAppSW.joA.geography.GeoRepository;
import com.mjuAppSW.joA.storage.CertifyNumManager;
import com.mjuAppSW.joA.storage.S3Uploader;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;
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
    private final CollegeRepository collegeRepository;
    private final JavaMailSender javaMailSender;
    private final CertifyNumManager certifyNumManager;
    private final S3Uploader s3Uploader;

    public UMailResponse sendCertifyNum(UMailRequest request) {
        College college = findByCollegeId(request.getCollegeId());
        if(college == null) return new UMailResponse(2); // 학교가 존재하지 않음

        Member findMember = findByUEmailAndCollege(request.getUEmail(), college);
        if(findMember != null) return new UMailResponse(1); // 이미 가입된 웹메일

        long randomId = getRandomId();
        String certifyNum = certifyNumManager.put(randomId);
        mail("인증번호", null, request.getUEmail(), college.getDomain(), certifyNum);
        return new UMailResponse(0, randomId); // 메일 정상적으로 발송
    }

    public boolean authCertifyNum(UNumRequest request) {
        if (certifyNumManager.compare(request.getId(), request.getCertifyNum())) {
            certifyNumManager.delete(request.getId());
            return true;
        }
        return false;
    }

    public StatusResponse verifyId(String loginId) {
        Member findMember = findByLoginId(loginId);
        if(findMember == null) return new StatusResponse(0);
        return new StatusResponse(1);
    }

    @Transactional
    public void join(JoinRequest request) {
        College college = findByCollegeId(request.getCollegeId());
        Member newMember = Member.builder().id(request.getId())
                                    .name(request.getName())
                                    .loginId(request.getLoginId())
                                    .password(request.getPassword())
                                    .uEmail(request.getUEmail())
                                    .college(college).build();
        Location newLocation = new Location(request.getId(), request.getCollegeId());
        memberRepository.save(newMember);
        geoRepository.save(newLocation);
    }

    public LoginResponse login(LoginRequest request) {
        Member findMember = findByLoginId(request.getLoginId());
        if(findMember == null) return new LoginResponse(1); // 존재하지 않는 id

        if(findMember.getPassword().equals(request.getPassword()))
            return new LoginResponse(0, findMember.getId()); // 정상 로그인

        return new LoginResponse(2); // 존재하지 않는 비밀번호
    }

    public StatusResponse findId(FindIdRequest request) {
        College college = findByCollegeId(request.getCollegeId());
        if(college == null) return new StatusResponse(1); // 학교 존재 X

        Member member = findByUEmailAndCollege(request.getUEmail(), college);
        if(member == null) return new StatusResponse(2); // 사용자 존재 x

        mail("id", member.getName(), member.getUEmail(), college.getDomain(), member.getLoginId());
        return new StatusResponse(0); // 정상 발송
    }

    @Transactional
    public StatusResponse findPassword(FindPasswordRequest request) {
        Member member = findByLoginId(request.getLoginId());
        if(member == null) return new StatusResponse(1); // 사용자 존재 X

        String random = randomPassword();
        mail("임시 비밀번호", member.getName(), member.getUEmail(), member.getCollege().getDomain(), random);
        member.changePassword(random);
        return new StatusResponse(0); // 임시 비밀번호 발송
    }

    @Transactional
    public StatusResponse transPassword(TransPasswordRequest request) {
        Member findMember = findByMemberId(request.getId());
        if(findMember == null) return new StatusResponse(2); // 사용자 존재 X

        if (findMember.getPassword().equals(request.getCurrentPassword())) {
            findMember.changePassword(request.getNewPassword());
            return new StatusResponse(0); // 정상 변경
        }
        return new StatusResponse(1); // 현재 비밀번호가 맞지 않음
    }

    public SetResponse set(Long id) {
        Member member = findByMemberId(id);
        if(member == null) return null;

        String urlCode = null;
        if(!member.getBasicProfile())
            urlCode = member.getUrlCode();

        return new SetResponse(member.getName(), urlCode);
    }

    public MyPageResponse sendMyPage(Long id) {
        Member member = findByMemberId(id);
        if(member == null) return null;

        int todayHeart = heartRepository.countTodayHeartsById(LocalDate.now(), member.getId());
        int totalHeart = heartRepository.countTotalHeartsById(member.getId());
        Pageable pageable = PageRequest.of(0, 3);
        List<String> voteTop3 = voteRepository.findVoteCategoryById(member.getId(), pageable);

        String urlCode = null;
        if(!member.getBasicProfile())
            urlCode = member.getUrlCode();

        return MyPageResponse.builder().name(member.getName())
                                .bio(member.getBio())
                                .urlCode(urlCode)
                                .todayHeart(todayHeart)
                                .totalHeart(totalHeart)
                                .voteTop3(voteTop3).build();
    }

    @Transactional
    public StatusResponse transName(NameRequest request) {
        Member member = findByMemberId(request.getId());
        if(member == null) return new StatusResponse(1); // 사용자 존재 X

        member.changeName(request.getName());
        return new StatusResponse(0);
    }

    @Transactional
    public boolean transBio(BioRequest request) {
        Member member = findByMemberId(request.getId());
        if(member == null) return false;

        member.changeBio(request.getBio());
        return true;
    }

    @Transactional
    public StatusResponse transPicture(PictureRequest request) {
        Member member = findByMemberId(request.getId());
        if(member == null) return new StatusResponse(2);

        s3Uploader.deletePicture(member.getUrlCode());
        String newUrlCode = s3Uploader.putPicture(member.getId(), request.getBase64Picture());
        member.changeUrlCode(newUrlCode);

        if(!newUrlCode.equals("error")) {
            if (member.getBasicProfile()) member.changeBasicProfileStatus(false);
            return new StatusResponse(0);
        }
        return new StatusResponse(1); // 업로드 안 된 경우
    }

    @Transactional
    public boolean deletePicture(MemberIdRequest request) {
        Member member = findByMemberId(request.getId());
        if(member == null) return false;

        s3Uploader.deletePicture(member.getUrlCode());
        member.changeUrlCode(null);
        member.changeBasicProfileStatus(true);
        return true;
    }

    @Transactional
    public StatusResponse withdrawal(MemberIdRequest request) {
        Long memberId = request.getId();
        Member member = findByMemberId(memberId);
        if(member == null) return new StatusResponse(2);

        if(s3Uploader.deletePicture(member.getUrlCode())) {
            geoRepository.deleteById(memberId);
            member.changeWithdrawalStatus(true);
            return new StatusResponse(0);
        }
        return new StatusResponse(1);
    }

    private static long getRandomId() {
        long min = 1000000000L;
        long max = 9999999999L;
        return ThreadLocalRandom.current().nextLong(min, max + 1);
    }

    private void mail(String header, String memberName, String uEmail, String collegeDomain, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(uEmail + collegeDomain);
        if(memberName == null) {
            message.setSubject("[JoA] " + header + "를 확인하세요.");
            message.setText(header + "는 " + content + " 입니다.");
        }
        else {
            message.setSubject("[JoA] " + memberName + "님의 " + header + "를 확인하세요.");
            message.setText(memberName + "님의 " + header + "는 " + content + " 입니다.");
        }
        javaMailSender.send(message);
    }

    private String randomPassword() {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = upper.toLowerCase();
        String digits = "0123456789";
        String specialChars = "!@#$%^&*()-_+=<>?";

        String allCharacters = upper + lower + digits + specialChars;
        int maxLength = 16;
        Random random = new SecureRandom();
        StringBuilder builder = new StringBuilder();

        while (builder.length() < maxLength) {
            int index = random.nextInt(allCharacters.length());
            char randomChar = allCharacters.charAt(index);
            builder.append(randomChar);
        }
        return builder.toString();
    }

    private Member findByMemberId(Long id) {
        return memberRepository.findById(id).orElse(null);
    }

    private Member findByUEmailAndCollege(String uEmail, College college) {
        return memberRepository.findByuEmailAndcollege(uEmail, college).orElse(null);
    }

    private Member findByLoginId(String loginId) {
        return memberRepository.findByloginId(loginId).orElse(null);
    }

    private College findByCollegeId(Long collegeId) {
        return collegeRepository.findById(collegeId).orElse(null);
    }

}
