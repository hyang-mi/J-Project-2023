package com.mjuAppSW.joA.domain.vote.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class VoteListResponse {
    private List<VoteContent> voteList;

    public VoteListResponse(List voteList) {
        this.voteList = voteList;
    }
}
