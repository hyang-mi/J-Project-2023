package com.mjuAppSW.appName.domain.member.geography.dto;

import com.mjuAppSW.appName.domain.member.geography.dto.NearByInfo;
import lombok.Data;

import java.util.List;

@Data
public class NearByList {
    private List<NearByInfo> nearByList;
}
