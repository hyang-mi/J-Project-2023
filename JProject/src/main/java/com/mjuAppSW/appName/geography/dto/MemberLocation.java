package com.mjuAppSW.appName.domain.member.geography.dto;

import lombok.Data;

@Data
public class MemberLocation {
    private Long id;
    private double latitude;
    private double longitude;

    public MemberLocation(Long id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
