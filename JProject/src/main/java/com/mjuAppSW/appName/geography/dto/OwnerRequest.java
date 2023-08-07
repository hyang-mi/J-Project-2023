package com.mjuAppSW.appName.geography.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class OwnerRequest {
    @JsonProperty("id")
    @NotBlank
    private Long id;
}
