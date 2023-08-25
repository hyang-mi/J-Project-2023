package com.mjuAppSW.joA.domain.message;

import com.mjuAppSW.joA.domain.member.Member;
import com.mjuAppSW.joA.domain.room.Room;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Message")
public class Message {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="Message_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name="Member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name="Room_id", nullable = false)
    private Room room;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    @Column(nullable = false)
    private String isChecked;

    public Message(long id){
        this.id = id;
    }

    public Message(Member member, Room room, String content, Date date, String isChecked) {
        this.member = member;
        this.room = room;
        this.content = content;
        this.time = date;
        this.isChecked = isChecked;
    }
}