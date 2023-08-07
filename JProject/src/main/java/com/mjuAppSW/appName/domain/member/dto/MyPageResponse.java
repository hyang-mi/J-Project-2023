package com.mjuAppSW.appName.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class MyPageResponse {
    private String name;
    private String base64Picture;
    private String bio;
    private Integer todayHeart;
    private Integer totalHeart;
    private List<String> voteTop3;
}
