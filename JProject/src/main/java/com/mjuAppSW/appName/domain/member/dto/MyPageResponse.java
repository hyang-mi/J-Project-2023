package com.mjuAppSW.appName.domain.member.dto;

import lombok.Data;

import java.util.List;

@Data
public class MyPageResponse {
    private String name;
    private String base64Image;
    private String introduce;
    private int todayHeart;
    private int totalHeart;
    private List<String> voteTop3;

    public MyPageResponse(String name, String introduce) {
        this.name = name;
        this.introduce = introduce;
    }
}
