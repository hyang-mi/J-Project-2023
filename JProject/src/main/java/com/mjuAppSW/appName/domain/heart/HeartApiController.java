package com.mjuAppSW.appName.domain.heart;

import com.mjuAppSW.appName.domain.heart.dto.HeartRequest;
import com.mjuAppSW.appName.domain.heart.dto.HeartResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class HeartApiController {

    private final HeartService heartService;

    @PostMapping("heart/send")
    public ResponseEntity<HeartResponse> sendHeart(HeartRequest request) {
        HeartResponse response = heartService.sendHeart(request.getGiveId(), request.getTakeId(), request.getNamed());
        if (response != null) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().build();
    }

}
