package com.mjuAppSW.appName.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

@Getter
public class UNumRequest {
    @JsonProperty("id")
    @NotBlank
    private Long id;

    @JsonProperty("certifyNum")
    @NotBlank
    private String certifyNum;
}
