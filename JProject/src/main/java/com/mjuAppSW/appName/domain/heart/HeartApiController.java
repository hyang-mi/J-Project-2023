package com.mjuAppSW.appName.domain.heart;

import com.mjuAppSW.appName.domain.heart.dto.HeartRequest;
import com.mjuAppSW.appName.domain.heart.dto.HeartResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class HeartApiController {

    private final HeartService heartService;

    @PostMapping("heart/send")
    public ResponseEntity<HeartResponse> sendHeart(@RequestBody @Valid HeartRequest request) {
        log.info("하트 전송 api 요청");
        log.info("giveId = {}, takeId = {}, named = {}", request.getGiveId(), request.getTakeId(), request.getNamed());
        HeartResponse response = heartService.sendHeart(request);
        Integer result = response.getStatus();
        if (result == 0)
            return ResponseEntity.ok(response);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
