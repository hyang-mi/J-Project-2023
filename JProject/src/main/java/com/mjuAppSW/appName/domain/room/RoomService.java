package com.mjuAppSW.appName.domain.room;

import com.mjuAppSW.appName.domain.room.dto.RoomResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RoomService {

    private RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository){
        this.roomRepository = roomRepository;
    }

    @Transactional
    public RoomResponse createRoom(){
        LocalDateTime currentDateTime = LocalDateTime.now();
        Room makeRoom = Room.createRoomItem(currentDateTime, "1");
        Room room = roomRepository.save(makeRoom);
        return new RoomResponse(room.getRoomId());
    }

    public boolean checkRoomId(Long roomId) {
        Optional<Room> findRoomId = roomRepository.findById(roomId);
        if(findRoomId.isPresent()){
            return true;
        }else {
            return false;
        }
    }

    public int checkCreateAtRoom(Long roomId){
        Optional<Room> checkCreatedAt = Optional.ofNullable(roomRepository.findByDate(roomId));
        if(checkCreatedAt.isPresent()){
            LocalDateTime date = checkCreatedAt.get().getDate();
            LocalDateTime currentDateTime = LocalDateTime.now();
            Duration duration = Duration.between(date, currentDateTime);
            long hours = duration.toHours();
            if(hours >= 24){
                return 0;
            }else{
                return 1;
            }
        }else{
            return 2;
        }
    }

    @Transactional
    public void updateRoom(Long roomId, String status){
        LocalDateTime time = LocalDateTime.now();
        roomRepository.updateCreatedAtAndStatus(roomId, time, status);
    }
}