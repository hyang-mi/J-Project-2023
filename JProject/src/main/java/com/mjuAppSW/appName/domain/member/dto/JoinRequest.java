package com.mjuAppSW.appName.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class JoinRequest {
    @JsonProperty("name")
    @NotBlank
    private String name;

    @JsonProperty("kId")
    @NotNull
    private Long kId;
}
