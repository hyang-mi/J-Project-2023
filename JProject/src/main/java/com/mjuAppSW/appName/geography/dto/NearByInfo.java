package com.mjuAppSW.appName.geography.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NearByInfo {
    private String name;
    private String urlCode;
    private String bio;
}
