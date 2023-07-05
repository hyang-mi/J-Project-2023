package com.mjuAppSW.appName.domain.member;

import lombok.Data;

@Data
public class KakaoUserResponse {
    private long id; // 카카오 회원번호
    private KakaoAccount kakaoAccount;
}
