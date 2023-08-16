package com.mjuAppSW.joA.domain.room.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class RoomCheckRequest {
    @JsonProperty("roomId")
    @NotNull
    private long roomId;
}
