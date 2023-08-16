package com.mjuAppSW.joA.domain.roomInMember;

import com.mjuAppSW.joA.domain.member.Member;
import com.mjuAppSW.joA.domain.room.Room;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name="Room_in_member")
@IdClass(RoomInMemberId.class)
@NoArgsConstructor
public class RoomInMember {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id")
    private Member member;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="roomId")
    private Room room;

    @Column(nullable = false)
    private String expired;

    @Column(nullable = false)
    private String result;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime entryTime;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime exitTime;

    public RoomInMember(Room room, Member member, String expired, String result) {
        this.room = room;
        this.member = member;
        this.expired = expired;
        this.result = result;
    }
}