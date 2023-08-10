package com.mjuAppSW.appName.domain.geography.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OwnerRequest {
    @JsonProperty("id")
    @NotNull
    private Long id;
}
