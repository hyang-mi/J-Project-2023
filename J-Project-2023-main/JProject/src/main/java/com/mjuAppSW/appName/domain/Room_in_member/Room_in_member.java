package com.mjuAppSW.appName.domain.Room_in_member;


import com.mjuAppSW.appName.domain.Message.Message;
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
    @ManyToOne
    @JoinColumn(name="Message_id")
    @Column(name="Message_id", nullable = false)
    private Message Message_id;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @Column(name="Room_id", nullable = false)
    private Room Room_id;

    @Column(name="Expired", nullable = false)
    private char Expired;

    @Column(name="Result", nullable = false)
    private char Result;

    @Column(name="Entry_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date Entry_time;

    @Column(name="Exit_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date Exit_time;
}


//public class Room_in_member {
//    @Id
//    @Column(name="Room_in_member_id", nullable = false)
//    private int Room_in_member_id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "Message_id")
//    @Column(name="Message_id", nullable = false)
//    private Message Message_id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn
//    @Column(name="Room_id", nullable = false)
//    private Room Room_id;
//
//    @Column(name="Expired", nullable = false)
//    private char Expired;
//
//    @Column(name="Result", nullable = false)
//    private char Result;
//
//    @Column(name="Entry_time")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date Entry_time;
//
//    @Column(name="Exit_time")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date Exit_time;
//}