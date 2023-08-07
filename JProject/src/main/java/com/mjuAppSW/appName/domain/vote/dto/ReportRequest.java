package com.mjuAppSW.appName.domain.vote.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

@Getter
public class ReportRequest {
    @JsonProperty("voteId")
    @NotBlank
    private Long voteId;

    @JsonProperty("reportId")
    @NotBlank
    private Long reportId;

    @JsonProperty("content")
    private String content;
}
