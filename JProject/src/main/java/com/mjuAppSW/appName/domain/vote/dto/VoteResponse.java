package com.mjuAppSW.appName.domain.vote;

import lombok.Data;

@Data
public class VoteResponse {
    private Integer status;

    public VoteResponse(Integer status) {
        this.status = status;
    }
}
