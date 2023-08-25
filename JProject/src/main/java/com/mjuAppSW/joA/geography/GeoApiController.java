package com.mjuAppSW.joA.geography;

import com.mjuAppSW.joA.geography.dto.IdRequest;
import com.mjuAppSW.joA.geography.dto.NearByListResponse;
import com.mjuAppSW.joA.geography.dto.StatusResponse;
import com.mjuAppSW.joA.geography.dto.UpdateRequest;
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
public class GeoApiController {

    private final GeoService geoService;

    @PostMapping("geo/update")
    public ResponseEntity<StatusResponse> updateLocation(@RequestBody @Valid UpdateRequest request) {
        log.info("위치 업데이트 api 요청");
        log.info("id = {}, latitude = {}, longitude = {}, altitude = {}",
                request.getId(), request.getLatitude(), request.getLongitude(), request.getAltitude());
        StatusResponse response = geoService.updateLocation(request);
        if(response.getStatus() == 0)
            return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("geo/get")
    public ResponseEntity<NearByListResponse> getNearByList(@RequestParam @NotNull Long id,
                                                            @RequestParam @NotBlank Double latitude,
                                                            @RequestParam @NotBlank Double longitude,
                                                            @RequestParam @NotBlank Double altitude) {
        log.info("주변 사람 불러오기 api 요청");
        log.info("id = {}, latitude = {}, longitude = {}, altitude = {}", id, latitude, longitude, altitude);
        UpdateRequest request = new UpdateRequest(id, latitude, longitude, altitude);

        NearByListResponse response = geoService.getNearByList(request);
        if(response != null) return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("geo/delete")
    public void deleteLocation(@RequestBody @Valid IdRequest request) {
        log.info("위치 삭제 api 요청");
        log.info("id = {}", request.getId());
        geoService.deleteLocation(request);
    }
}
