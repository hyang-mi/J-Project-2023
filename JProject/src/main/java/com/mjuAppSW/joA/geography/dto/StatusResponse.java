package com.mjuAppSW.joA.geography.dto;

import lombok.Getter;

@Getter
public class StatusResponse {
    private Integer status;

    public StatusResponse(Integer status) {
        this.status = status;
    }
}
