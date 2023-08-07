package com.mjuAppSW.appName.domain.heart.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class HeartResponse {
    private Integer status;
    private Boolean isMatched;
    private String giveName;
    private String giveBase64Picture;
    private String takeBase64Picture;

    public HeartResponse(Integer status) {
        this.status = status;
    }

    @Builder
    public HeartResponse(Integer status, Boolean isMatched, String giveName, String giveBase64Picture, String takeBase64Picture) {
        this.status = status;
        this.isMatched = isMatched;
        this.giveName = giveName;
        this.giveBase64Picture = giveBase64Picture;
        this.takeBase64Picture = takeBase64Picture;
    }
}
