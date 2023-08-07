package com.mjuAppSW.appName.geography.dto;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
public class NearByListResponse {
    private List<NearByInfo> nearByList;

    public NearByListResponse(List<NearByInfo> nearByList) {
        this.nearByList = nearByList;
    }
}
