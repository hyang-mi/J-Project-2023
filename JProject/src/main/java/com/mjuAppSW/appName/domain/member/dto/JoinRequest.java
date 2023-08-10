package com.mjuAppSW.appName.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

@Getter
public class LoginRequest {
    @JsonProperty("name")
    @NotBlank
    private String name;

    @JsonProperty("kId")
    @NotNull
    private Long kId;
}
