package com.mjuAppSW.joA.domain.roomInMember.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class CheckVoteRequest {
    @JsonProperty("roomId")
    @NotNull
    private long roomId;
}
