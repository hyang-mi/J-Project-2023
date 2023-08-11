package com.mjuAppSW.appName.domain.message;

import com.mjuAppSW.appName.domain.member.Member;
import com.mjuAppSW.appName.domain.room.Room;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Message {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    public Long message_Id;

    @ManyToOne
    @JoinColumn(name="memberId")
    private Member member;

    @ManyToOne
    @JoinColumn(name="roomId")
    private Room room;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    @Column(nullable = false)
    private String isChecked;

    public Message(long messageId){
        this.message_Id = messageId;
    }

    public Message(Member member, Room room, String content, Date date, String isChecked) {
        this.member = member;
        this.room = room;
        this.content = content;
        this.time = date;
        this.isChecked = isChecked;
    }
}