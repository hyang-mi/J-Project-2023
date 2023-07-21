package com.mjuAppSW.appName.domain.member.geography;

import com.mjuAppSW.appName.domain.member.geography.dto.MemberLocation;
import com.mjuAppSW.appName.domain.member.geography.dto.NearByInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;


@Controller
@RequiredArgsConstructor
@Slf4j
public class GeoController {

    private final GeoService geoService;

    // 내 위치 업데이트 (5m 기준)
    @MessageMapping("/geo/update")
    public void updateLocation(MemberLocation request) {
        geoService.updateLocation(request);
    }

    // 저장된 위치 기준 반경 100m 내에서 가장 가까운 순으로 50명 반환
    @MessageMapping("/geo/get")
    @SendTo("/geo/get/list")
    public List<NearByInfo> getNearByList(MemberLocation request) {
        return geoService.getNearByList(request);
    }
}
