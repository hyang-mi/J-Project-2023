package com.mjuAppSW.appName.domain.vote;

import com.mjuAppSW.appName.domain.vote.dto.OwnerRequest;
import com.mjuAppSW.appName.domain.vote.dto.ReportRequest;
import com.mjuAppSW.appName.domain.vote.dto.VoteListResponse;
import com.mjuAppSW.appName.domain.vote.dto.SendRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class VoteApiController {
    private final VoteService voteService;

    @PostMapping("/vote/send")
    public ResponseEntity<Integer> sendVote(@RequestBody SendRequest request) {
        Integer response = voteService.sendVote(request);
        if(response == 0)
            return ResponseEntity.ok(response);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @GetMapping("/vote/get")
    public ResponseEntity<VoteListResponse> getVotes(@RequestBody OwnerRequest request) {
        VoteListResponse response = voteService.getVotes(request);
        if (response != null)
            return ResponseEntity.ok(response);
        else
            return ResponseEntity.badRequest().build();
    }

    @PostMapping("/vote/report")
    public ResponseEntity<Integer> reportVote(@RequestBody ReportRequest request) {
        Integer response = voteService.reportVote(request);
        if(response == 0)
            return ResponseEntity.ok(response);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
