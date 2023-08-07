package com.mjuAppSW.appName.domain.vote;

import com.mjuAppSW.appName.domain.vote.dto.ReportRequest;
import com.mjuAppSW.appName.domain.vote.dto.VoteListResponse;
import com.mjuAppSW.appName.domain.vote.dto.SendRequest;
import com.mjuAppSW.appName.domain.vote.dto.VoteResponse;
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
    public ResponseEntity<VoteResponse> sendVote(@RequestBody @Valid SendRequest request) {
        log.info("투표 보내기 api 요청");
        log.info("giveId = {}, takeId = {}, categoryId = {}, hint = {}", request.getGiveId(), request.getTakeId(), request.getCategoryId(), request.getHint());
        VoteResponse response = voteService.sendVote(request);
        if(response.getStatus() == 0)
            return ResponseEntity.ok(response);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
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
    public ResponseEntity<VoteResponse> reportVote(@RequestBody @Valid ReportRequest request) {
        log.info("투표 신고하기 api 요청");
        log.info("voteId = {}, reportId = {}, content = {}", request.getVoteId(), request.getReportId(), request.getContent());
        VoteResponse response = voteService.reportVote(request);
        if(response.getStatus() == 0)
            return ResponseEntity.ok(response);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
