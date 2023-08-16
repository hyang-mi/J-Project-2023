package com.mjuAppSW.joA.domain.vote;

import com.mjuAppSW.joA.domain.vote.dto.ReportRequest;
import com.mjuAppSW.joA.domain.vote.dto.VoteListResponse;
import com.mjuAppSW.joA.domain.vote.dto.SendRequest;
import com.mjuAppSW.joA.domain.vote.dto.StatusResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class VoteApiController {
    private final VoteService voteService;

    @PostMapping("/vote/send")
    public ResponseEntity<StatusResponse> sendVote(@RequestBody @Valid SendRequest request) {
        log.info("투표 보내기 api 요청");
        log.info("giveId = {}, takeId = {}, categoryId = {}, hint = {}", request.getGiveId(), request.getTakeId(), request.getCategoryId(), request.getHint());
        StatusResponse response = voteService.sendVote(request);
        return returnStatusResponse(response);
    }

    @GetMapping("/vote/get")
    public ResponseEntity<VoteListResponse> getVotes(@RequestParam("takeId") Long takeId) {
        log.info("투표 목록 가져오기 api 요청");
        log.info("takeId = {}", takeId);
        VoteListResponse response = voteService.getVotes(takeId);
        if (response != null)
            return ResponseEntity.ok(response);
        else
            return ResponseEntity.badRequest().build();
    }

    @PostMapping("/vote/report")
    public ResponseEntity<StatusResponse> reportVote(@RequestBody @Valid ReportRequest request) {
        log.info("투표 신고하기 api 요청");
        log.info("voteId = {}, reportId = {}, content = {}", request.getVoteId(), request.getReportId(), request.getContent());
        StatusResponse response = voteService.reportVote(request);
        return returnStatusResponse(response);
    }

    private ResponseEntity<StatusResponse> returnStatusResponse(StatusResponse response) {
        if (response.getStatus() == 0) return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response);
    }
}
