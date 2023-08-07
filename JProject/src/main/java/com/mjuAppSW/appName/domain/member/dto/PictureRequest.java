package com.mjuAppSW.appName.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

@Getter
public class PictureRequest {
    @JsonProperty("id")
    @NotBlank
    private Long id;

    @JsonProperty("base64Picture")
    @NotBlank
    private String base64Picture;
}
