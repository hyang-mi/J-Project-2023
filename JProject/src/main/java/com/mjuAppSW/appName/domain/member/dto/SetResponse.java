package com.mjuAppSW.appName.domain.member.dto;

import lombok.Data;

import java.sql.Blob;

@Data
public class SetResponse {
    private String name;
    private String base64Image;
}
