package com.mjuAppSW.joA.domain.member;

import com.mjuAppSW.joA.domain.college.MCollege;
import com.mjuAppSW.joA.domain.college.MCollegeRepository;
import com.mjuAppSW.joA.domain.heart.HeartRepository;
import com.mjuAppSW.joA.domain.member.dto.*;
import com.mjuAppSW.joA.domain.vote.VoteRepository;
import com.mjuAppSW.joA.geography.college.PCollege;
import com.mjuAppSW.joA.geography.college.PCollegeRepository;
import com.mjuAppSW.joA.geography.location.LocationRepository;
import com.mjuAppSW.joA.geography.location.Location;
import com.mjuAppSW.joA.storage.ExpiredManager;
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
    private final LocationRepository geoRepository;
    private final MCollegeRepository mCollegeRepository;
    private final PCollegeRepository pCollegeRepository;
    private final JavaMailSender javaMailSender;
    private final ExpiredManager expiredManager;
    private final S3Uploader s3Uploader;

    public UMailResponse sendCertifyNum(UMailRequest request) {
        MCollege college = findByCollegeId(request.getCollegeId());
        if (college == null) return new UMailResponse(3); // 학교가 존재하지 않음

        Member findMember = findByUEmailAndCollege(request.getUEmail(), college);
        if (findMember != null) return new UMailResponse(1); // 이미 가입된 웹메일

        boolean certifyEmail = expiredManager.isExistedValue("CU",
                request.getUEmail() + college.getDomain());
        boolean joinEmail = expiredManager.isExistedValue("U",
                request.getUEmail() + college.getDomain());
        if(joinEmail || certifyEmail) return new UMailResponse(2); // 회원가입 중인 이메일

        long randomId = getRandomId();
        String certifyNum = getRandomCertifyNum();
        expiredManager.add("C" + randomId, certifyNum, 7);
        expiredManager.add("CU" + randomId, request.getUEmail()+college.getDomain(), 7);
        mail("인증번호", null, request.getUEmail(), college.getDomain(), certifyNum);
        return new UMailResponse(0, randomId); // 메일 정상적으로 발송
    }

    public StatusResponse authCertifyNum(UNumRequest request) {
        MCollege college = findByCollegeId(request.getCollegeId());
        if (college == null) return new StatusResponse(3); // 학교가 존재하지 않음
        Long memberId = request.getId();
        String webMail = request.getUEmail() + college.getDomain();

        if(!expiredManager.compare("CU" + memberId, webMail)) {
            return new StatusResponse(2); // 이전 인증 웹메일과 일치하지 않음
        }

        if (expiredManager.compare("C" + memberId, request.getCertifyNum())) {
            expiredManager.delete("C" + memberId);
            expiredManager.delete("CU" + memberId);
            expiredManager.add("U" + memberId, webMail, 60);
            return new StatusResponse(0);
        }
        return new StatusResponse(1); // 유효하지 않은 인증번호
    }

    public StatusResponse verifyId(Long id, String loginId) {
        if (!expiredManager.isExistedKey("U" + id)) {
            return new StatusResponse(3);
        }

        Member findMember = findByLoginId(loginId);
        if (findMember == null) {
            if(!expiredManager.isExistedValue("I", loginId)) {
                expiredManager.add("I" + id, loginId, 30);
                expiredManager.changeTime("U" + id, 30);
                return new StatusResponse(0);
            }
            else {
                if(expiredManager.compare("I" + id, loginId))
                    return new StatusResponse(0);
                else
                    return new StatusResponse(2);
            }
        }
        return new StatusResponse(1);
    }

    @Transactional
    public StatusResponse join(JoinRequest request) {
        Long memberId = request.getId();
        if(!expiredManager.isExistedKey("U" + memberId))
            return new StatusResponse(1);

        if(!expiredManager.isExistedKey("I" + memberId) || !expiredManager.compare("I" + memberId, request.getLoginId()))
            return new StatusResponse(2);

        String loginId = expiredManager.getSavedValue("I" + memberId);

        String savedMailAddress = expiredManager.getSavedValue("U" + memberId);
        String[] split = savedMailAddress.split("@");
        String uEmail = split[0];
        MCollege college = mCollegeRepository.findBydomain("@" + split[1]).orElse(null);

        Member newMember = Member.builder().id(memberId)
                .name(request.getName())
                .loginId(loginId)
                .password(request.getPassword())
                .uEmail(uEmail)
                .college(college).build();


        PCollege pCollege = pCollegeRepository.findById(college.getId()).orElse(null);
        if(pCollege == null) return new StatusResponse(3);
        Location newLocation = new Location(memberId, pCollege);

        memberRepository.save(newMember);
        geoRepository.save(newLocation);

        expiredManager.delete("I" + memberId);
        expiredManager.delete("U" + memberId);
        return new StatusResponse(0);
    }

    public LoginResponse login(LoginRequest request) {
        Member findMember = findByLoginId(request.getLoginId());
        if (findMember == null) return new LoginResponse(1); // 존재하지 않는 id

        if (findMember.getPassword().equals(request.getPassword()))
            return new LoginResponse(0, findMember.getId()); // 정상 로그인

        return new LoginResponse(2); // 존재하지 않는 비밀번호
    }

    public StatusResponse findId(FindIdRequest request) {
        MCollege college = findByCollegeId(request.getCollegeId());
        if (college == null) return new StatusResponse(1); // 학교 존재 X

        Member member = findByUEmailAndCollege(request.getUEmail(), college);
        if (member == null) return new StatusResponse(2); // 사용자 존재 x

        mail("id", member.getName(), member.getUEmail(), college.getDomain(), member.getLoginId());
        return new StatusResponse(0); // 정상 발송
    }

    @Transactional
    public StatusResponse findPassword(FindPasswordRequest request) {
        Member member = findByLoginId(request.getLoginId());
        if (member == null) return null; // 사용자 존재 X

        String random = randomPassword();
        mail("임시 비밀번호", member.getName(), member.getUEmail(), member.getCollege().getDomain(), random);
        member.changePassword(random);
        return new StatusResponse(0); // 임시 비밀번호 발송
    }

    @Transactional
    public StatusResponse transPassword(TransPasswordRequest request) {
        Member findMember = findByMemberId(request.getId());
        if (findMember == null) return new StatusResponse(2); // 사용자 존재 X

        if (findMember.getPassword().equals(request.getCurrentPassword())) {
            findMember.changePassword(request.getNewPassword());
            return new StatusResponse(0); // 정상 변경
        }
        return new StatusResponse(1); // 현재 비밀번호가 맞지 않음
    }

    public SetResponse set(Long id) {
        Member member = findByMemberId(id);
        if (member == null) return null;

        String urlCode = "";
        if (!member.getBasicProfile())
            urlCode = member.getUrlCode();

        return new SetResponse(member.getName(), urlCode);
    }

    public MyPageResponse sendMyPage(Long id) {
        Member member = findByMemberId(id);
        if (member == null) return null;

        int todayHeart = heartRepository.countTodayHeartsById(LocalDate.now(), member.getId());
        int totalHeart = heartRepository.countTotalHeartsById(member.getId());
        Pageable pageable = PageRequest.of(0, 3);
        List<String> voteTop3 = voteRepository.findVoteCategoryById(member.getId(), pageable);

        String urlCode = "";
        if (!member.getBasicProfile())
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
        if (member == null) return new StatusResponse(1); // 사용자 존재 X

        member.changeName(request.getName());
        return new StatusResponse(0);
    }

    @Transactional
    public boolean transBio(BioRequest request) {
        Member member = findByMemberId(request.getId());
        if (member == null) return false;

        member.changeBio(request.getBio());
        return true;
    }

    @Transactional
    public StatusResponse transPicture(PictureRequest request) {
        Member member = findByMemberId(request.getId());
        if (member == null) return new StatusResponse(2);

        if(!member.getBasicProfile())
            s3Uploader.deletePicture(member.getUrlCode());
        String newUrlCode = s3Uploader.putPicture(member.getId(), request.getBase64Picture());
        member.changeUrlCode(newUrlCode);

        if (!newUrlCode.equals("error")) {
            if (member.getBasicProfile()) member.changeBasicProfileStatus(false);
            return new StatusResponse(0);
        }
        return new StatusResponse(1); // 업로드 안 된 경우
    }

    @Transactional
    public boolean deletePicture(MemberIdRequest request) {
        Member member = findByMemberId(request.getId());
        if (member == null) return false;

        s3Uploader.deletePicture(member.getUrlCode());
        member.changeUrlCode("");
        member.changeBasicProfileStatus(true);
        return true;
    }

    @Transactional
    public StatusResponse withdrawal(MemberIdRequest request) {
        Long memberId = request.getId();
        Member member = findByMemberId(memberId);
        if (member == null) return new StatusResponse(2);

        if (s3Uploader.deletePicture(member.getUrlCode())) {
            geoRepository.deleteById(memberId);
            member.changeWithdrawalStatus(true);
            return new StatusResponse(0);
        }
        return new StatusResponse(1);
    }

    private String getRandomCertifyNum() {
        int random = ThreadLocalRandom.current().nextInt(100000, 1000000);
        return String.valueOf(random);
    }

    private long getRandomId() {
        long min = 1000000000L;
        long max = 9999999999L;
        return ThreadLocalRandom.current().nextLong(min, max + 1);
    }

    private void mail(String header, String memberName, String uEmail, String collegeDomain, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(uEmail + collegeDomain);
        if (memberName == null) {
            message.setSubject("[JoA] " + header + "를 확인하세요.");
            message.setText(header + "는 " + content + " 입니다.");
        } else {
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
        Member member = memberRepository.findById(id).orElse(null);
        if (member.getWithdrawal() == true) return null;
        return member;
    }

    private Member findByUEmailAndCollege(String uEmail, MCollege college) {
        Member member = memberRepository.findByuEmailAndcollege(uEmail, college).orElse(null);
        if (member.getWithdrawal() == true) return null;
        return member;
    }

    private Member findByLoginId(String loginId) {
        Member member = memberRepository.findByloginId(loginId).orElse(null);
        if (member.getWithdrawal() == true) return null;
        return member;
    }

    private MCollege findByCollegeId(Long collegeId) {
        return mCollegeRepository.findById(collegeId).orElse(null);
    }
}
