package com.mjuAppSW.appName.domain.member;

import com.mjuAppSW.appName.domain.member.dto.*;
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

    @PostMapping("/join")
    public ResponseEntity<JoinResponse> join(@RequestBody @Valid JoinRequest request) {
        log.info("회원가입 api 요청");
        log.info("name = {}, kId = {}", request.getName(), request.getKId());
        JoinResponse response = memberService.join(request);
        return ResponseEntity.ok(response);
    }

    // 로그인, 로그아웃

    @PostMapping("/mail/send")
    public ResponseEntity<StatusResponse> sendCertifyNum(@RequestBody @Valid UMailRequest request) {
        log.info("메일 전송 api 요청");
        log.info("id = {}, email = {} ", request.getId(), request.getUEmail());

        StatusResponse response = memberService.sendCertifyNum(request);
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
        return HttpStatus.BAD_REQUEST;
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

    @PostMapping("/set/myPage/picture")
    public ResponseEntity<StatusResponse> transPicture(@RequestBody @Valid PictureRequest request) {
        log.info("프로필 사진 변경 api 요청");
        log.info("id = {}, base64Image is null = {}", request.getId(), request.getBase64Picture().isEmpty());

        StatusResponse response = memberService.transPicture(request);
        if (response.getStatus() == 0) return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/set/myPage/picture/delete")
    public HttpStatus deletePicture(@RequestBody @Valid IdRequest request) {
        log.info("프로필 사진 삭제 api 요청");
        log.info("id = {}", request.getId());

        boolean result = memberService.deletePicture(request);
        if (result) return HttpStatus.OK;
        return HttpStatus.BAD_REQUEST;
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
    public HttpStatus withdrawal(@RequestBody @Valid IdRequest request) {
        log.info("회원 탈퇴 api 요청");
        log.info("id = {}", request.getId());

        memberService.withdrawal(request);
        return HttpStatus.OK;
    }
}
