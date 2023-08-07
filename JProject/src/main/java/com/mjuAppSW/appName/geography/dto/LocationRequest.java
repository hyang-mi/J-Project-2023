package com.mjuAppSW.appName.geography.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;

@Getter
public class LocationRequest {

    @JsonProperty("id")
    @NotBlank
    private Long id;

    @JsonProperty("latitude")
    @NotBlank
    private Double latitude;

    @JsonProperty("longitude")
    @NotBlank
    private Double longitude;

    @JsonProperty("altitude")
    @NotBlank
    private Double altitude;

    public LocationRequest(Long id, Double latitude, Double longitude, Double altitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }
}
