package com.mjuAppSW.appName.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

@Getter
public class UMailRequest {
    @JsonProperty("id")
    @NotNull
    private Long id;

    @JsonProperty("uEmail")
    @NotBlank
    private String uEmail;

    @JsonProperty("collegeId")
    private Long collegeId;
}
