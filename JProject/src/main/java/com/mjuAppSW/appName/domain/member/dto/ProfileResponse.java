package com.mjuAppSW.appName.domain.member.dto;

import lombok.Data;

@Data
public class ProfileResponse {
    private String name;
    private String base64Image;
    private String introduce;

    public ProfileResponse(String name, String base64Image, String introduce) {
        this.name = name;
        this.base64Image = base64Image;
        this.introduce = introduce;
    }
}
