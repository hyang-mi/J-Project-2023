package com.mjuAppSW.appName.domain.heart.dto;

import lombok.Data;

@Data
public class HeartResponse {
    private Integer status;
    private Boolean isMatched;
    private String giveName;
    private String giveBase64Image;
    private String takeBase64Image;

    public HeartResponse(Integer status) {
        this.status = status;
    }

    public HeartResponse(Integer status, Boolean isMatched, String giveName, String giveBase64Image, String takeBase64Image) {
        this.status = status;
        this.isMatched = isMatched;
        this.giveName = giveName;
        this.giveBase64Image = giveBase64Image;
        this.takeBase64Image = takeBase64Image;
    }
}
