package com.mjuAppSW.appName.geography.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NearByInfo {
    private String name;
    private String base64Picture;
    private String bio;
}
