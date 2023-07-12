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
@Table(name="Message")
public class Message {
    @Id
    @Column(nullable = false)
    private long messageId;

    @ManyToOne
    @JoinColumn(name="memberId")
    private Member memberId;

    @ManyToOne
    @JoinColumn(name="roomId")
    private Room roomId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    @Column(nullable = false)
    private char isChecked;
}