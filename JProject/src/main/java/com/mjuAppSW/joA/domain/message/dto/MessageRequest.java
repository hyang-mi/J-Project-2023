package com.mjuAppSW.joA.domain.message.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class MessageRequest {
    @JsonProperty("roomId")
    @NotNull
    private long roomId;
    @JsonProperty("memberId")
    @NotNull
    private long memberId;
}
