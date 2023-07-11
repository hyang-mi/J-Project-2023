package com.mjuAppSW.appName.domain.member;

import com.mjuAppSW.appName.domain.member.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberApiController {

    private final MemberService memberService;

    /**
     * 카카오톡 로그인
     *
     * @param request [LoginRequest] String accessToken
     * @return [LoginResponse] Long id, String name, String uEmail(nullable)
     */
    @GetMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse member = memberService.login(request.getAccessToken());
        if (member != null)
            return ResponseEntity.ok(member); // 학교 이메일 data 없으면 null로 응답
        return ResponseEntity.badRequest().build(); // 사용자 정보 조회 요청 실패 or 유효하지 않은 토큰
    }

    /**
     * 학교 메일로 인증번호 전송
     *
     * @param request [UMailRequest] Long id, String uEmail
     * @return [UMailResponse] String certifyNum
     */
    @PostMapping("/mail/send")
    public ResponseEntity<UMailResponse> sendCertifyNum(@RequestBody UMailRequest request) {
        UMailResponse response = memberService.sendCertifyNum(request.getUEmail(), request.getId());
        if (response != null)
            return ResponseEntity.ok(response);
        return ResponseEntity.notFound().build(); // 해당 사용자가 존재하지 않음
    }

    /**
     * 인증번호 확인
     *
     * @param request [UNumRequest] Long id, String uEmail, String certifyNum
     * @return [HttpStatus] OK, BAD_REQUEST
     */
    @PostMapping("/mail/auth")
    public HttpStatus authUmail(@RequestBody UNumRequest request) {
        boolean isCorrected = memberService.authCertifyNum(request.getCertifyNum(), request.getUEmail(), request.getId());
        if (isCorrected)
            return HttpStatus.OK;
        return HttpStatus.BAD_REQUEST; // 사용자가 존재하지 않거나, 인증 번호가 유효하지 않음
    }

    /**
     * 설정 메뉴 클릭
     *
     * @param request [MemberRequset] Long id
     * @return [SetResponse] String name, String base64Image(Nullable)
     */
    @GetMapping("/set")
    public ResponseEntity<SetResponse> set(@RequestBody MemberRequest request) {
        SetResponse response = memberService.set(request.getId());
        if (response != null)
            return ResponseEntity.ok(response);
        return ResponseEntity.notFound().build(); // 해당 사용자가 존재하지 않음
    }

    /**
     * 회원 탈퇴
     *
     * @param request [MemberRequest] Long id
     * @return [HttpStatus] OK / BAD_REQUEST
     */
    @PostMapping("/set/withdrawal")
    public HttpStatus withdrawal(@RequestBody MemberRequest request) {
        boolean result = memberService.withdrawal(request.getId());
        if (result) {
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST; // 사용자가 존재하지 않거나, 탈퇴가 완료되지 않았음
    }

    /**
     * 마이페이지 클릭
     *
     * @param request [MemberRequset] Long id
     * @return String name; String base64Image; String introduce; private int todayHeart; private int totalHeart; private List<String> voteTop3;
     */
    @GetMapping("/set/myPage")
    public ResponseEntity<MyPageResponse> sendMyPage(@RequestBody MemberRequest request) {
        MyPageResponse response = memberService.sendMyPage(request.getId());
        if (response != null) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build(); // 해당 사용자가 존재하지 않음
    }

    /**
     * 프로필 사진 변경 클릭
     *
     * @param request Long id; String encodedImage;
     * @return [HttpStatus] OK / BAD_REQUEST
     */
    @PostMapping("/set/myPage/picture")
    public HttpStatus transImage(@RequestBody ImageRequest request) {
        boolean result = memberService.transImage(request.getId(), request.getEncodedImage());
        if (result) return HttpStatus.OK;
        return HttpStatus.BAD_REQUEST; // id or 프로필 사진 경로가 유효하지 않은 경우
    }

    /**
     * 프로필 사진 삭제 클릭
     *
     * @param request Long id; String encodedImage;
     * @return [HttpStatus] OK / BAD_REQUEST
     */
    @PostMapping("/set/myPage/picture/delete")
    public HttpStatus deleteImage(@RequestBody ImageRequest request) {
        boolean result = memberService.transImage(request.getId(), request.getEncodedImage());
        if (result) return HttpStatus.OK;
        return HttpStatus.BAD_REQUEST; // id or 프로필 사진 경로가 유효하지 않은 경우
    }

    /**
     * 한줄 소개 변경 클릭
     *
     * @param request Long id; String introduce;
     * @return [HttpStatus] OK / BAD_REQUEST
     */
    @PostMapping("/set/myPage/introduce")
    public HttpStatus transIntroduce(@RequestBody IntroduceRequest request) {
        boolean result = memberService.transIntroduce(request.getId(), request.getIntroduce());
        if (result) return HttpStatus.OK;
        return HttpStatus.BAD_REQUEST;
    }

    @GetMapping("/list/profile")
    public ResponseEntity<ProfileResponse> getOtherProfile(@RequestBody MemberRequest request) {
        ProfileResponse response = memberService.getOtherProfile(request.getId());
        if(response != null)
            return ResponseEntity.ok(response);
        return ResponseEntity.notFound().build(); // 해당 사용자가 존재하지 않음
    }
}
