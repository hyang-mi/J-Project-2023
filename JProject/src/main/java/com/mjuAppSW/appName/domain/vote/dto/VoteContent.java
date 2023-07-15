package com.mjuAppSW.appName.domain.vote.dto;

import lombok.Data;

@Data
public class VoteContent {
    private Long voteId;
    private Long categoryId;
    private String hint;

    public VoteContent(Long voteId, Long categoryId, String hint) {
        this.voteId = voteId;
        this.categoryId = categoryId;
        this.hint = hint;
    }
}
