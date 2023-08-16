package com.mjuAppSW.joA.domain.room;

import com.mjuAppSW.joA.domain.roomInMember.RoomInMember;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name="Room")
public class Room{
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long roomId;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime date;

    @Column(nullable = false)
    private String status;

    @OneToMany(mappedBy = "room")
    private List<RoomInMember> roomInMember = new ArrayList<>();

    public Room(LocalDateTime date, String status) {
        this.date = date;
        this.status = status;
    }

    public Room(long roomId) {
        this.roomId = roomId;
    }


    public static Room createRoomItem(LocalDateTime date, String c){
        Room room = new Room();
        room.setDate(date);
        room.setStatus(c);
        return room;
    }
}