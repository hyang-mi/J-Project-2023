package com.mjuAppSW.appName.domain.member;

import com.mjuAppSW.appName.domain.college.College;
import com.mjuAppSW.appName.domain.college.CollegeRepository;
import com.mjuAppSW.appName.geography.Location;
import com.mjuAppSW.appName.domain.heart.HeartRepository;
import com.mjuAppSW.appName.domain.member.dto.*;
import com.mjuAppSW.appName.domain.vote.VoteRepository;
import com.mjuAppSW.appName.geography.GeoRepository;
import com.mjuAppSW.appName.storage.CertifyNumManager;
import com.mjuAppSW.appName.storage.S3Uploader;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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

    @Transactional
    public JoinResponse join(JoinRequest request) {
        Optional<Member> findMember = memberRepository.findBykId(request.getKId());
        Member member = findMember.get();
        if(member != null && !member.getWithdrawal()) return new JoinResponse(1);

        long memberId = getRandomId();
        Member newMember = new Member(memberId, request.getName(), request.getKId());
        memberRepository.save(newMember);
        return new JoinResponse(0, memberId, request.getName());
    }

    private static long getRandomId() {
        long min = 1000000000L;
        long max = 9999999999L;
        return ThreadLocalRandom.current().nextLong(min, max + 1);
    }

    // 로그인, 로그아웃

    @Transactional
    public StatusResponse sendCertifyNum(UMailRequest request) {
        Member findMember = memberRepository.findByuEmail(request.getUEmail()).orElse(null);
        if(findMember != null && findMember.getCollege() != null) {
            if(findMember.getCollege().getId() == request.getCollegeId())
                return new StatusResponse(1);
        }

        Member member = memberRepository.findById(request.getId()).orElse(null);
        College college = collegeRepository.findById(request.getCollegeId()).orElse(null);
        if(member == null || college == null) return new StatusResponse(2);

        String certifyNum = certifyNumManager.put(request.getId());
        sendMail(request.getUEmail(), college.getDomain(), certifyNum);

        return new StatusResponse(0);
    }

    public void sendMail(String uEmail, String collegeDomain, String certifyNum) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(uEmail + collegeDomain);
        message.setSubject("[앱이름] 인증번호를 확인하세요.");
        message.setText("인증번호는 [" + certifyNum + "] 입니다.");
        javaMailSender.send(message);
    }

    @Transactional
    public Boolean authCertifyNum(UNumRequest request) {
        Long memberId = request.getId();
        Member member = memberRepository.findById(memberId).orElse(null);
        College college = collegeRepository.findById(request.getCollegeId()).orElse(null);
        if(member == null || college == null) return false;

        if (certifyNumManager.compare(memberId, request.getCertifyNum())) {
            certifyNumManager.delete(memberId);
            member.setUEmailAndCollege(request.getUEmail(), college);
            Location location = new Location(request.getId(), college.getId());
            geoRepository.save(location);
            return true;
        }
        return false;
    }

    public SetResponse set(Long id) {
        Member member = memberRepository.findById(id).orElse(null);
        if(member == null) return null;

        String urlCode = null;
        if(!member.getBasicProfile())
            urlCode = member.getUrlCode();

        return SetResponse.builder().name(member.getName())
                                    .urlCode(urlCode).build();
    }

    public MyPageResponse sendMyPage(Long id) {
        Member member = memberRepository.findById(id).orElse(null);
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
    public StatusResponse transPicture(PictureRequest request) {
        Member member = memberRepository.findById(request.getId()).orElse(null);
        if(member == null) return new StatusResponse(2);

        s3Uploader.deletePicture(member.getUrlCode());
        String newUrlCode = s3Uploader.putPicture(String.valueOf(member.getId()), request.getBase64Picture());
        member.changeUrlCode(newUrlCode);

        if(!newUrlCode.equals("error")) {
            if (member.getBasicProfile()) member.changeBasicProfileStatus(false);
            return new StatusResponse(0);
        }
        return new StatusResponse(1); // 업로드 안 된 경우
    }

    @Transactional
    public boolean deletePicture(IdRequest request) {
        Member member = memberRepository.findById(request.getId()).orElse(null);
        if(member == null) return false;

        s3Uploader.deletePicture(member.getUrlCode());
        member.changeUrlCode(null);
        member.changeBasicProfileStatus(true);
        return true;
    }

    @Transactional
    public boolean transBio(BioRequest request) {
        Member member = memberRepository.findById(request.getId()).orElse(null);
        if(member == null) return false;

        member.changeBio(request.getBio());
        return true;
    }

    @Transactional
    public void withdrawal(IdRequest request) {
        Long memberId = request.getId();
        memberRepository.setWithdrawalById(memberId);
        geoRepository.deleteById(memberId);
        // 사진 삭제할까? 말까?
    }
}
