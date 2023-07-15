package com.mjuAppSW.appName.domain.vote.dto;

import lombok.Data;

@Data
public class SendRequest {
    private Long giveId;
    private Long takeId;
    private Long CategoryId;
    private String hint;
}
