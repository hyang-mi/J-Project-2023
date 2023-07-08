package com.mjuAppSW.appName.domain.Message;
import com.mjuAppSW.appName.domain.Room_in_member.Room_in_member;
import com.mjuAppSW.appName.domain.Member.Member;
import com.mjuAppSW.appName.domain.Room.Room;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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