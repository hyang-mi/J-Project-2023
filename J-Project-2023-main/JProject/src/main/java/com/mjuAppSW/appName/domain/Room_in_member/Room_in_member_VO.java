package com.mjuAppSW.appName.domain.Room_in_member;

import com.mjuAppSW.appName.domain.Member.Member;
import com.mjuAppSW.appName.domain.Room.Room;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Room_in_member_VO {
    private Member member;
    private Room room;
}