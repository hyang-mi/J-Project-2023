package com.mjuAppSW.appName.domain.member.dto;

import lombok.Getter;

@Getter
public class LoginResponse {
    private Long id;
    private String name;
    private String uEmail;

    public LoginResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public LoginResponse(Long id, String name, String uEmail) {
        this.id = id;
        this.name = name;
        this.uEmail = uEmail;
    }
}
