package com.mjuAppSW.appName.domain.vote.dto;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
public class VoteListResponse {
    private List<VoteContent> voteList;

    public VoteListResponse(List voteList) {
        this.voteList = voteList;
    }
}
