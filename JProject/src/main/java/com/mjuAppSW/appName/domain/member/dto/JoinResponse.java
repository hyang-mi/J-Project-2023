package com.mjuAppSW.appName.domain.member.dto;

import lombok.Getter;

@Getter
public class JoinResponse {
    private Integer status;
    private Long id;
    private String name;

    public JoinResponse(Integer status) {
        this.status = status;
    }

    public JoinResponse(Integer status, Long id, String name) {
        this.status = status;
        this.id = id;
        this.name = name;
    }
}
