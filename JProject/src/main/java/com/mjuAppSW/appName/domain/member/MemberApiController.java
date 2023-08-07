package com.mjuAppSW.appName.domain.member;

import com.mjuAppSW.appName.domain.member.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = memberService.login(request);
        if (response != null)
            return ResponseEntity.ok(response); // 학교 이메일 data 없으면 null로 응답
        return ResponseEntity.badRequest().build(); // 사용자 정보 조회 요청 실패 or 유효하지 않은 토큰
    }

    @PostMapping("/mail/send")
    public ResponseEntity<MemberResponse> sendCertifyNum(@RequestBody @Valid UMailRequest request) {
        log.info("메일 전송 api 요청");
        log.info("id = {}, email = {} ", request.getId(), request.getUEmail());

        MemberResponse response = memberService.sendCertifyNum(request);
        if(response.getStatus() == 0)
            return ResponseEntity.ok(response);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @PostMapping("/mail/auth")
    public HttpStatus authCertifyNum(@RequestBody @Valid UNumRequest request) {
        log.info("메일 인증 api 요청");
        log.info("id = {}, certifyNum = {} ", request.getId(), request.getCertifyNum());

        Boolean result = memberService.authCertifyNum(request);
        if (result) {
            log.info("return ok");
            return HttpStatus.OK;
        }
        log.info("return bad request");
        return HttpStatus.BAD_REQUEST; // 사용자가 존재하지 않거나, 인증 번호가 유효하지 않음
    }

    @GetMapping ("/set")
    public ResponseEntity<SetResponse> set(@RequestParam Long id) {
        log.info("설정 api 요청");
        log.info("id = {}", id);

        SetResponse response = memberService.set(id);
        if (response != null)
            return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().build(); // 해당 사용자가 존재하지 않음
    }

    @GetMapping("/set/myPage")
    public ResponseEntity<MyPageResponse> sendMyPage(@RequestParam Long id) {
        log.info("마이페이지 api 요청");
        log.info("id = {}", id);

        MyPageResponse response = memberService.sendMyPage(id);
        if (response != null) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().build(); // 해당 사용자가 존재하지 않음
    }

    @PostMapping("/set/myPage/picture")
    public ResponseEntity<MemberResponse> transPicture(@RequestBody @Valid PictureRequest request) {
        log.info("프로필 사진 변경 api 요청");
        log.info("id = {}, base64Image is null = {}", request.getId(), request.getBase64Picture().isEmpty());

        MemberResponse response = memberService.transPicture(request);
        if (response.getStatus() == 0) return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response); // 해당 사용자가 존재하지 않음
    }

    @PostMapping("/set/myPage/picture/delete")
    public HttpStatus deletePicture(@RequestBody @Valid MemberRequest request) {
        log.info("프로필 사진 삭제 api 요청");
        log.info("id = {}", request.getId());

        boolean result = memberService.deletePicture(request);
        if (result) return HttpStatus.OK;
        return HttpStatus.BAD_REQUEST; // 해당 사용자가 존재하지 않음
    }

    @PostMapping("/set/myPage/bio")
    public HttpStatus transBio(@RequestBody @Valid BioRequest request) {
        log.info("한 줄 소개 변경 api 요청");
        log.info("id = {}, bio = {}", request.getId(), request.getBio());

        boolean result = memberService.transBio(request);
        if (result) return HttpStatus.OK;
        return HttpStatus.BAD_REQUEST; // 해당 사용자가 존재하지 않음
    }

    @PostMapping("/set/withdrawal")
    public HttpStatus withdrawal(@RequestBody @Valid MemberRequest request) {
        log.info("회원 탈퇴 api 요청");
        log.info("id = {}", request.getId());

        memberService.withdrawal(request);
        return HttpStatus.OK;
    }

//    @GetMapping("/list/profile") // 주변 사람 불러오기로 통일
//    public ResponseEntity<ProfileResponse> getOtherProfile(@RequestParam Long id) {
//        ProfileResponse response = memberService.getOtherProfile(id);
//        if(response != null)
//            return ResponseEntity.ok(response);
//        return ResponseEntity.badRequest().build(); // 해당 사용자가 존재하지 않음
//    }
}
