package com.mjuAppSW.appName.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponse {
    private Integer status;

    public MemberResponse(Integer status) {
        this.status = status;
    }
}
