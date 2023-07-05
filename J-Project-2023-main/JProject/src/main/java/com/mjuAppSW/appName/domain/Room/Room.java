package com.mjuAppSW.appName.domain.Room;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name="Room")
public class Room{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Room_id", nullable = false)
    public int Room_id;

    @Column(name="Date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date Date;

    @Column(name="Status", nullable = false)
    public char Status;

    @OneToMany(mappedBy = "Room")
    private List<com.mjuAppSW.appName.domain.Room_in_member.Room_in_member> Room_in_member = new ArrayList<>();


    public static Room createRoomItem(Date date, char c){
        Room room = new Room();
        room.setDate(date);
        room.setStatus(c);
        return room;
    }
}