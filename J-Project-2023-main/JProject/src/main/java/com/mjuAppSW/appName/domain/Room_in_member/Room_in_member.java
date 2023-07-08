package com.mjuAppSW.appName.domain.Room_in_member;

import com.mjuAppSW.appName.domain.Member.Member;
import com.mjuAppSW.appName.domain.Room.Room;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Setter
@Getter
@Table(name="Room_in_member")
@IdClass(Room_in_member_id.class)
@NoArgsConstructor
public class Room_in_member{
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id")
    private Member memberId;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="roomId")
    private Room roomId;

    @Column(nullable = false)
    private char expired;

    @Column(nullable = false)
    private char result;

    @Temporal(TemporalType.TIMESTAMP)
    private Date entryTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date exitTime;


    public static Room_in_member createRoomItem(Room room, Member member, char expired, char result){
        Room_in_member room_in_member = new Room_in_member();
        room_in_member.setRoomId(room);
        room_in_member.setMemberId(member);
        room_in_member.setExpired(expired);
        room_in_member.setResult(result);
        return room_in_member;
    }
}