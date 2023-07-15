package com.mjuAppSW.appName.domain.member;

import com.mjuAppSW.appName.domain.member.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse member = memberService.login(request);
        if (member != null)
            return ResponseEntity.ok(member); // 학교 이메일 data 없으면 null로 응답
        return ResponseEntity.badRequest().build(); // 사용자 정보 조회 요청 실패 or 유효하지 않은 토큰
    }

    @PostMapping("/mail/send")
    public ResponseEntity<UMailResponse> sendCertifyNum(@RequestBody UMailRequest request) {
        UMailResponse response = memberService.sendCertifyNum(request);
        if (response != null)
            return ResponseEntity.ok(response);
        return ResponseEntity.notFound().build(); // 해당 사용자가 존재하지 않음
    }

    @PostMapping("/mail/auth")
    public HttpStatus authUmail(@RequestBody UNumRequest request) {
        boolean isCorrected = memberService.authCertifyNum(request);
        if (isCorrected)
            return HttpStatus.OK;
        return HttpStatus.BAD_REQUEST; // 사용자가 존재하지 않거나, 인증 번호가 유효하지 않음
    }

    @GetMapping("/set")
    public ResponseEntity<SetResponse> set(@RequestBody MemberRequest request) {
        SetResponse response = memberService.set(request);
        if (response != null)
            return ResponseEntity.ok(response);
        return ResponseEntity.notFound().build(); // 해당 사용자가 존재하지 않음
    }

    @PostMapping("/set/withdrawal")
    public HttpStatus withdrawal(@RequestBody MemberRequest request) {
        memberService.withdrawal(request);
        return HttpStatus.OK;
    }

    @GetMapping("/set/myPage")
    public ResponseEntity<MyPageResponse> sendMyPage(@RequestBody MemberRequest request) {
        MyPageResponse response = memberService.sendMyPage(request);
        if (response != null) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build(); // 해당 사용자가 존재하지 않음
    }

    @PostMapping("/set/myPage/picture")
    public HttpStatus transImage(@RequestBody ImageRequest request) {
        boolean result = memberService.transPicture(request);
        if (result) return HttpStatus.OK;
        return HttpStatus.BAD_REQUEST; // id or 프로필 사진 경로가 유효하지 않은 경우
    }

    @PostMapping("/set/myPage/picture/delete")
    public HttpStatus deleteImage(@RequestBody MemberRequest request) {
        boolean result = memberService.deletePicture(request);
        if (result) return HttpStatus.OK;
        return HttpStatus.BAD_REQUEST; // 해당 사용자가 존재하지 않음
    }

    @PostMapping("/set/myPage/introduce")
    public HttpStatus transIntroduce(@RequestBody IntroduceRequest request) {
        boolean result = memberService.transIntroduce(request);
        if (result) return HttpStatus.OK;
        return HttpStatus.BAD_REQUEST;
    }

    @GetMapping("/list/profile")
    public ResponseEntity<ProfileResponse> getOtherProfile(@RequestBody MemberRequest request) {
        ProfileResponse response = memberService.getOtherProfile(request);
        if(response != null)
            return ResponseEntity.ok(response);
        return ResponseEntity.notFound().build(); // 해당 사용자가 존재하지 않음
    }
}
