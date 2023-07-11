package com.mjuAppSW.appName.domain.vote.dto;

import lombok.Data;

import java.util.List;

@Data
public class RandomVoteResponse {
    private List<String> voteNameList;
}
