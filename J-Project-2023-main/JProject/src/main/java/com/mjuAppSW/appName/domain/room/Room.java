package com.mjuAppSW.appName.domain.room;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mjuAppSW.appName.domain.room_in_member.Room_in_member;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name="Room")
public class Room{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    public long roomId;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date date;

    @Column(nullable = false)
    public char status;

    @OneToMany(mappedBy = "roomId")
    private List<Room_in_member> roomInMember = new ArrayList<>();

    public Room(Date date, char status) {
        this.date = date;
        this.status = status;
    }

    public Room(long roomId) {
        this.roomId = roomId;
    }

    public static Room createRoomItem(Date date, char c){
        Room room = new Room();
        room.setDate(date);
        room.setStatus(c);
        return room;
    }
}