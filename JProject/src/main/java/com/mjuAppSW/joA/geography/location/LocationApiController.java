package com.mjuAppSW.joA.geography.location;

import com.mjuAppSW.joA.geography.location.dto.NearByListResponse;
import com.mjuAppSW.joA.geography.location.dto.PolygonRequest;
import com.mjuAppSW.joA.geography.location.dto.UpdateResponse;
import com.mjuAppSW.joA.geography.location.dto.UpdateRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LocationApiController {

    private final LocationService locationService;

    @PostMapping("geo/update")
    public ResponseEntity<UpdateResponse> updateLocation(@RequestBody @Valid UpdateRequest request) {
        log.info("updateLocation : id = {}, latitude = {}, longitude = {}, altitude = {}", request.getId(), request.getLatitude(), request.getLongitude(), request.getAltitude());
        UpdateResponse response = locationService.updateLocation(request);
        log.info("updateLocation Return : OK, status = {}, isContained = {}", response.getStatus(), response.getIsContained());
        return ResponseEntity.ok(response);
    }

    @GetMapping("geo/get")
    public ResponseEntity<NearByListResponse> getNearByList(@RequestParam @NotNull Long id,
                                                            @RequestParam @NotBlank Double latitude,
                                                            @RequestParam @NotBlank Double longitude,
                                                            @RequestParam @NotBlank Double altitude) {
        log.info("getNearByList : id = {}, latitude = {}, longitude = {}, altitude = {}", id, latitude, longitude, altitude);
        UpdateRequest request = new UpdateRequest(id, latitude, longitude, altitude);
        NearByListResponse response = locationService.getNearByList(request);
        if(response != null) {
            log.info("getNearByList Return : OK, nearByList size = {}", response.getNearByList().size());
            return ResponseEntity.ok(response);
        }
        log.warn("getNearByList Return : BAD_REQUEST, member id is not valid");
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("geo/set/polygon")
    public void setPolygon(@RequestBody @Valid PolygonRequest request) {
        locationService.setPolygon(request);
    }

//    @PostMapping("geo/delete")
//    public void deleteLocation(@RequestBody @Valid IdRequest request) {
//        log.info("위치 삭제 api 요청");
//        log.info("id = {}", request.getId());
//        geoService.deleteLocation(request);
//    }
}
