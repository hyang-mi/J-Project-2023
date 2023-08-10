package com.mjuAppSW.appName.domain.geography;

import com.mjuAppSW.appName.domain.geography.dto.NearByListResponse;
import com.mjuAppSW.appName.domain.geography.dto.OwnerRequest;
import com.mjuAppSW.appName.domain.geography.dto.LocationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class GeoApiController {

    private final GeoService geoService;

    @PostMapping("geo/update")
    public HttpStatus updateLocation(@RequestBody @Valid LocationRequest request) {
        log.info("위치 업데이트 api 요청");
        log.info("id = {}, latitude = {}, longitude = {}, altitude = {}",
                request.getId(), request.getLatitude(), request.getLongitude(), request.getAltitude());
        geoService.updateLocation(request);
        return HttpStatus.OK;
    }

    @GetMapping("geo/get")
    public NearByListResponse getNearByList(@RequestParam Long id, @RequestParam Double latitude,
                                            @RequestParam Double longitude, @RequestParam Double altitude) {
        log.info("주변 사람 불러오기 api 요청");
        log.info("id = {}, latitude = {}, longitude = {}, altitude = {}", id, latitude, longitude, altitude);
        LocationRequest request = new LocationRequest(id, latitude, longitude, altitude);
        return geoService.getNearByList(request);
    }

    @PostMapping("geo/delete")
    public void deleteLocation(@RequestBody @Valid OwnerRequest request) {
        log.info("위치 삭제 api 요청");
        log.info("id = {}", request.getId());
        geoService.deleteLocation(request);
    }
}
