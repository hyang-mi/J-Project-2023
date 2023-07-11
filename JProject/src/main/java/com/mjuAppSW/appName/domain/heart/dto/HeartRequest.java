package com.mjuAppSW.appName.domain.heart.dto;

import lombok.Data;

@Data
public class HeartRequest {
    private Long giveId;
    private Long takeId;
    private Boolean named;
}
