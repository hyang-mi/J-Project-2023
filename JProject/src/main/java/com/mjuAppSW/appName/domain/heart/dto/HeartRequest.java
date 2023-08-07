package com.mjuAppSW.appName.domain.heart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class HeartRequest {
    @JsonProperty("giveId")
    @NotBlank
    private Long giveId;

    @JsonProperty("takeId")
    @NotBlank
    private Long takeId;

    @JsonProperty("named")
    @NotBlank
    private Boolean named;
}
