package com.mjuAppSW.appName.domain.geography.dto;

import lombok.Data;

import java.util.List;

@Data
public class NearByListResponse {
    private List<NearByInfo> nearByList;
}
