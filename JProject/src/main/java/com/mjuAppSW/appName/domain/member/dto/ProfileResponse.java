package com.mjuAppSW.appName.domain.member.dto;

import lombok.Data;

@Data
public class ProfileResponse {
    private String name;
    private String base64Picture;
    private String introduce;

    public ProfileResponse(String name, String base64Picture, String introduce) {
        this.name = name;
        this.base64Picture = base64Picture;
        this.introduce = introduce;
    }
}
