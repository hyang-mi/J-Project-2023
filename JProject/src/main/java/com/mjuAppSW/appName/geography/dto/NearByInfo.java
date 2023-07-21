package com.mjuAppSW.appName.domain.member.geography.dto;

import lombok.Data;

@Data
public class NearByInfo {
    private String name;
    private String base64Picture;
    private String bio;

    public NearByInfo(String name, String base64Picture, String bio) {
        this.name = name;
        this.base64Picture = base64Picture;
        this.bio = bio;
    }
}
