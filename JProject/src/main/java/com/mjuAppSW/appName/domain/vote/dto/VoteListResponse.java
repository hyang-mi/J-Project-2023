package com.mjuAppSW.appName.domain.vote.dto;

import lombok.Data;

import java.util.List;

@Data
public class VoteListResponse {
    List<VoteContent> voteList;

    public VoteListResponse(List voteList) {
        this.voteList = voteList;
    }
}
