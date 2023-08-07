package com.mjuAppSW.appName.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SetResponse {
    private String name;
    private String base64Picture;
}
