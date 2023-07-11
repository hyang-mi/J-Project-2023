package com.mjuAppSW.appName.domain.heart.dto;

import lombok.Data;

@Data
public class HeartResponse {
    private Boolean isMatched;
    private String giveName;
    private String giveBase64Image;
    private String takeBase64Image;

    public HeartResponse(Boolean isMatched, String giveName, String giveBase64Image, String takeBase64Image) {
        this.isMatched = isMatched;
        this.giveName = giveName;
        this.giveBase64Image = giveBase64Image;
        this.takeBase64Image = takeBase64Image;
    }
}
