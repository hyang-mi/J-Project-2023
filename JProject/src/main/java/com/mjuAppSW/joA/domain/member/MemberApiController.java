package com.mjuAppSW.joA.domain.member;

import com.mjuAppSW.joA.domain.member.dto.*;
import jakarta.validation.Valid;
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

    @PostMapping("/mail/send")
    public ResponseEntity<UMailResponse> sendCertifyNum(@RequestBody @Valid UMailRequest request) {
        log.info("메일 전송 api 요청");
        log.info("collegeId = {}, email = {} ", request.getCollegeId(), request.getUEmail());

        UMailResponse response = memberService.sendCertifyNum(request);
        if (response.getStatus() == 0)
            return ResponseEntity.ok(response);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @PostMapping("/mail/auth")
    public HttpStatus authCertifyNum(@RequestBody @Valid UNumRequest request) {
        log.info("메일 인증 api 요청");
        log.info("id = {}, certifyNum = {} ", request.getId(), request.getCertifyNum());

        boolean result = memberService.authCertifyNum(request);
        return returnHttpStatus(result);
    }

    @GetMapping("/join/verify/id")
    public ResponseEntity<StatusResponse> verifyId(@RequestParam String loginId) {
        log.info("id 중복 검증 api 요청");
        log.info("login id = {}", loginId);

        StatusResponse response = memberService.verifyId(loginId);
        return returnStatusResponse(response);
    }

    @PostMapping("/join")
    public void join(@RequestBody @Valid JoinRequest request) {
        log.info("회원가입 api 요청");
        log.info("name = {}, loginId = {}", request.getName(), request.getLoginId());

        memberService.join(request);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        log.info("로그인 api 요청");
        log.info("id = {}, password = {}", request.getLoginId(), request.getPassword());

        LoginResponse response = memberService.login(request);
        if(response.getStatus() == 0)
            return ResponseEntity.ok(response);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @PostMapping("/login/find/id")
    public ResponseEntity<StatusResponse> findId(@RequestBody @Valid FindIdRequest request) {
        log.info("아이디 찾기 api 요청");
        log.info("college Id = {}, uEmail = {}", request.getCollegeId(), request.getUEmail());

        StatusResponse response = memberService.findId(request);
        return returnStatusResponse(response);
    }

    @PostMapping("/login/find/password")
    public ResponseEntity<StatusResponse> findPassword(@RequestBody @Valid FindPasswordRequest request) {
        log.info("비밀번호 찾기 api 요청");
        log.info("Login Id = {}", request.getLoginId());

        StatusResponse response = memberService.findPassword(request);
        return returnStatusResponse(response);
    }

    @PostMapping("/login/password")
    public ResponseEntity<StatusResponse> transPassword(@RequestBody @Valid TransPasswordRequest request) {
        log.info("비밀번호 변경 api 요청");
        log.info("id = {}, current password = {}, new password = {}", request.getId(), request.getCurrentPassword(), request.getNewPassword());

        StatusResponse response = memberService.transPassword(request);
        return returnStatusResponse(response);
    }

    @GetMapping ("/set")
    public ResponseEntity<SetResponse> set(@RequestParam Long id) {
        log.info("설정 api 요청");
        log.info("id = {}", id);

        SetResponse response = memberService.set(id);
        if (response != null)
            return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/set/myPage")
    public ResponseEntity<MyPageResponse> sendMyPage(@RequestParam Long id) {
        log.info("마이페이지 api 요청");
        log.info("id = {}", id);

        MyPageResponse response = memberService.sendMyPage(id);
        if (response != null) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/set/myPage/name")
    public ResponseEntity<StatusResponse> transName(@RequestBody @Valid NameRequest request) {
        log.info("이름 변경 api 요청");
        log.info("id = {}, name = {}", request.getId(), request.getName());

        StatusResponse response = memberService.transName(request);
        return returnStatusResponse(response);
    }

    @PostMapping("/set/myPage/bio")
    public HttpStatus transBio(@RequestBody @Valid BioRequest request) {
        log.info("한 줄 소개 변경 api 요청");
        log.info("id = {}, bio = {}", request.getId(), request.getBio());

        boolean result = memberService.transBio(request);
        return returnHttpStatus(result);
    }

    @PostMapping("/set/myPage/picture")
    public ResponseEntity<StatusResponse> transPicture(@RequestBody @Valid PictureRequest request) {
        log.info("프로필 사진 변경 api 요청");
        log.info("id = {}, base64Image is null = {}", request.getId(), request.getBase64Picture().isEmpty());

        StatusResponse response = memberService.transPicture(request);
        return returnStatusResponse(response);
    }

    @PostMapping("/set/myPage/picture/delete")
    public HttpStatus deletePicture(@RequestBody @Valid MemberIdRequest request) {
        log.info("프로필 사진 삭제 api 요청");
        log.info("id = {}", request.getId());

        boolean result = memberService.deletePicture(request);
        return returnHttpStatus(result);
    }

    @PostMapping("/set/withdrawal")
    public ResponseEntity<StatusResponse> withdrawal(@RequestBody @Valid MemberIdRequest request) {
        log.info("회원 탈퇴 api 요청");
        log.info("id = {}", request.getId());

        StatusResponse response = memberService.withdrawal(request);
        return returnStatusResponse(response);
    }

    private HttpStatus returnHttpStatus(boolean result) {
        if (result) return HttpStatus.OK;
        return HttpStatus.BAD_REQUEST;
    }

    private ResponseEntity<StatusResponse> returnStatusResponse(StatusResponse response) {
        if (response.getStatus() == 0) return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response);
    }
}
