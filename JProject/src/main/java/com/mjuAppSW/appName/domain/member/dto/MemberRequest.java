package com.mjuAppSW.appName.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MemberRequest {
    @JsonProperty("id")
    private Long id;
}
