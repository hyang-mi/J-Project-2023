package com.mjuAppSW.appName.domain.member.dto;

import lombok.Getter;

@Getter
public class LoginResponse {
    private Long id;
    private String name;

    public LoginResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
