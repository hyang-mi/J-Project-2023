package com.mjuAppSW.appName.domain.Message;

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
    @Column(name="Message_id", nullable = false)
    private int Message_id;

    @ManyToOne
    @JoinColumn(name="Member_id")
    @Column(name="Member_id", nullable = false)
    private Member Member_id;

    @ManyToOne
    @JoinColumn(name="Room_id")
    @Column(name="Room_id", nullable = false)
    private Room Room_id;

    @Column(name="Content", nullable = false)
    private String Content;

    @Column(name="Time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date Time;

    @Column(name="Is_checked", nullable = false)
    private char Is_checked;

    @OneToMany(mappedBy = "Message")
    private List<com.mjuAppSW.appName.domain.Room_in_member.Room_in_member> Room_in_member = new ArrayList<>();
}