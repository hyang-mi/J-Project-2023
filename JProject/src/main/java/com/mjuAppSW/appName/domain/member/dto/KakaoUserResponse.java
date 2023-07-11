package com.mjuAppSW.appName.domain.member.dto;

import lombok.Data;

@Data
public class KakaoUserResponse {
    private Long kId; // 카카오 회원번호
    private KakaoAccount kakaoAccount;
}
