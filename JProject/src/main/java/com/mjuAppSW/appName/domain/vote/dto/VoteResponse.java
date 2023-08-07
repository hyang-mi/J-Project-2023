package com.mjuAppSW.appName.domain.vote.dto;

import lombok.Getter;

@Getter
public class VoteResponse {
    private Integer status;

    public VoteResponse(Integer status) {
        this.status = status;
    }
}
