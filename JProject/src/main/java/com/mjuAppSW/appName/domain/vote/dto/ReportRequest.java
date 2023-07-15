package com.mjuAppSW.appName.domain.vote.dto;

import lombok.Data;

@Data
public class ReportRequest {
    private Long voteId;
    private Long categoryId;
    private String content;
}
