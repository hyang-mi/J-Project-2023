package com.mjuAppSW.appName.domain.member.dto;

import lombok.Data;

import java.util.List;

@Data
public class MyPageResponse {
    private String name;
    private String base64Picture;
    private String introduce;
    private Integer todayHeart;
    private Integer totalHeart;
    private List<String> voteTop3;

    public MyPageResponse(String name, String introduce) {
        this.name = name;
        this.introduce = introduce;
    }
}
