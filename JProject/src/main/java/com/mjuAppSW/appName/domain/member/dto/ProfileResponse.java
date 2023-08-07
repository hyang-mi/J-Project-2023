package com.mjuAppSW.appName.domain.member.dto;

import lombok.Getter;

@Getter
public class ProfileResponse {
    private String name;
    private String base64Picture;
    private String bio;

    public ProfileResponse(String name, String base64Picture, String bio) {
        this.name = name;
        this.base64Picture = base64Picture;
        this.bio = bio;
    }
}
