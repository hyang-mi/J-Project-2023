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
        log.info("sendCertifyNum : collegeId = {}, email = {}", request.getCollegeId(), request.getUEmail());
        UMailResponse response = memberService.sendCertifyNum(request);
        log.info("sendCertifyNum Return : OK, status = {}, id = {}", response.getStatus(), response.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/mail/auth")
    public ResponseEntity<StatusResponse> authCertifyNum(@RequestBody @Valid UNumRequest request) {
        log.info("authCertifyNum : id = {}, certifyNum = {}, uEmail = {}, collegeId = {}", request.getId(), request.getCertifyNum(), request.getUEmail(), request.getCollegeId());
        StatusResponse response = memberService.authCertifyNum(request);
        log.info("authCertifyNum Return : OK, status = {}", response.getStatus());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/join/verify/id")
    public ResponseEntity<StatusResponse> verifyId(@RequestParam Long id, @RequestParam String loginId) {
        log.info("verifyId : id = {}, login id = {}", id, loginId);
        StatusResponse response = memberService.verifyId(id, loginId);
        log.info("verifyId Return : OK, status = {}", response.getStatus());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/join")
    public ResponseEntity<StatusResponse> join(@RequestBody @Valid JoinRequest request) {
        log.info("join : id = {}, name = {}, loginId = {}, password ={}", request.getId(), request.getName(), request.getLoginId(), request.getPassword());
        StatusResponse response = memberService.join(request);
        log.info("join Return : OK, status = {}", response.getStatus());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        log.info("login : id = {}, password = {}", request.getLoginId(), request.getPassword());
        LoginResponse response = memberService.login(request);
        log.info("login Return : OK, status = {}, id = {}", response.getStatus(), response.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login/find/id")
    public ResponseEntity<StatusResponse> findId(@RequestBody @Valid FindIdRequest request) {
        log.info("findId : college Id = {}, uEmail = {}", request.getCollegeId(), request.getUEmail());
        StatusResponse response = memberService.findId(request);
        log.info("findId Return : OK, status = {}", response.getStatus());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login/find/password")
    public ResponseEntity<StatusResponse> findPassword(@RequestBody @Valid FindPasswordRequest request) {
        log.info("findPassword : Login Id = {}", request.getLoginId());
        StatusResponse response = memberService.findPassword(request);
        log.info("findPassword Return : OK, status = {}", response.getStatus());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login/password")
    public ResponseEntity<StatusResponse> transPassword(@RequestBody @Valid TransPasswordRequest request) {
        log.info("transPassword : id = {}, current password = {}, new password = {}", request.getId(), request.getCurrentPassword(), request.getNewPassword());
        StatusResponse response = memberService.transPassword(request);
        log.info("transPassword Return : OK, status = {}", response.getStatus());
        return ResponseEntity.ok(response);
    }

    @GetMapping ("/set")
    public ResponseEntity<SetResponse> set(@RequestParam Long id) {
        log.info("set : id = {}", id);
        SetResponse response = memberService.set(id);
        if (response != null) {
            log.info("set Return : OK, name = {}, urlCode = {}", response.getName(), response.getUrlCode());
            return ResponseEntity.ok(response);
        }
        log.warn("set Return : BAD_REQUEST, member id is not valid");
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/set/myPage")
    public ResponseEntity<MyPageResponse> sendMyPage(@RequestParam Long id) {
        log.info("sendMyPage : id = {}", id);
        MyPageResponse response = memberService.sendMyPage(id);
        if (response != null) {
            log.info("sendMyPage Return : OK, name = {}, urlCode = {}, bio = {}, todayHeart = {}, totalHeart = {}, voteTop3 size = {}", response.getName(), response.getUrlCode(), response.getBio(), response.getTodayHeart(), response.getTotalHeart(), response.getVoteTop3().size());
            return ResponseEntity.ok(response);
        }
        log.warn("sendMyPage Return : BAD_REQUEST, member id is not valid");
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/set/myPage/name")
    public ResponseEntity<StatusResponse> transName(@RequestBody @Valid NameRequest request) {
        log.info("transName : id = {}, name = {}", request.getId(), request.getName());
        StatusResponse response = memberService.transName(request);
        log.info("transName Return : OK, status = {}", response.getStatus());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/set/myPage/bio")
    public HttpStatus transBio(@RequestBody @Valid BioRequest request) {
        log.info("transBio : id = {}, bio = {}", request.getId(), request.getBio());
        boolean result = memberService.transBio(request);
        if (result) {
            log.info("transBio Return : OK");
            return HttpStatus.OK;
        }
        log.warn("transBio Return : BAD_REQUEST, member id is not valid");
        return HttpStatus.BAD_REQUEST;
    }

    @PostMapping("/set/myPage/picture")
    public ResponseEntity<StatusResponse> transPicture(@RequestBody @Valid PictureRequest request) {
        log.info("transPicture : id = {}, base64Image is null = {}", request.getId(), request.getBase64Picture().isEmpty());
        StatusResponse response = memberService.transPicture(request);
        log.info("transPicture Return : OK, status = {}", response.getStatus());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/set/myPage/picture/delete")
    public HttpStatus deletePicture(@RequestBody @Valid MemberIdRequest request) {
        log.info("deletePicture : id = {}", request.getId());
        boolean result = memberService.deletePicture(request);
        if (result) {
            log.info("deletePicture Return : OK");
            return HttpStatus.OK;
        }
        log.warn("deletePicture Return : BAD_REQUEST, member id is not valid");
        return HttpStatus.BAD_REQUEST;
    }

    @PostMapping("/set/withdrawal")
    public ResponseEntity<StatusResponse> withdrawal(@RequestBody @Valid MemberIdRequest request) {
        log.info("withdrawal : id = {}", request.getId());
        StatusResponse response = memberService.withdrawal(request);
        log.info("withdrawal Return : OK, status = {}", response.getStatus());
        return ResponseEntity.ok(response);
    }

//    private HttpStatus returnHttpStatus(boolean result) {
//        if (result) return HttpStatus.OK;
//        return HttpStatus.BAD_REQUEST;
//    }

//    private ResponseEntity<StatusResponse> returnStatusResponse(StatusResponse response) {
//        if (response.getStatus() == 0) return ResponseEntity.ok(response);
//        return ResponseEntity.badRequest().body(response);
//    }
}
