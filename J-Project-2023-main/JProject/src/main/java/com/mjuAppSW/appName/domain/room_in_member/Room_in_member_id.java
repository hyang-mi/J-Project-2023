package com.mjuAppSW.appName.domain.room_in_member;

import lombok.Data;

import java.io.Serializable;

@Data

public class Room_in_member_id implements Serializable {
    private long memberId;
    private long roomId;
    @Override
    public int hashCode(){
      return super.hashCode();
    }

    @Override
    public boolean equals(Object obj){
        return super.equals(obj);
    }
}