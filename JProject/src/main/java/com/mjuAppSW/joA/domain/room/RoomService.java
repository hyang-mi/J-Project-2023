package com.mjuAppSW.joA.domain.room;

import com.mjuAppSW.joA.domain.room.dto.RoomResponse;
import com.mjuAppSW.joA.domain.roomInMember.RoomInMemberRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RoomService {

    private RoomRepository roomRepository;
    private RoomInMemberRepository roomInMemberRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository, RoomInMemberRepository roomInMemberRepository){
        this.roomRepository = roomRepository;
        this.roomInMemberRepository = roomInMemberRepository;
    }

    @Transactional
    public RoomResponse createRoom(){
        LocalDateTime currentDateTime = LocalDateTime.now();
        Room makeRoom = Room.createRoomItem(currentDateTime, "1");
        Room room = roomRepository.save(makeRoom);
        return new RoomResponse(room.getId());
    }

    public Boolean checkRoomId(Long roomId) {
        Optional<Room> findRoomId = roomRepository.findById(roomId);
        if(findRoomId.isPresent()){
            return true;
        }else {
            return false;
        }
    }

    public Long calculationHour(LocalDateTime getTime){
        LocalDateTime currentDateTime = LocalDateTime.now();
        Duration duration = Duration.between(getTime, currentDateTime);
        Long hours = duration.toHours();
        return hours;
    }

    public Integer checkCreateAtRoom(Long roomId){
        Optional<Room> checkCreatedAt = Optional.ofNullable(roomRepository.findByDate(roomId));
        if(checkCreatedAt.isPresent()){
            LocalDateTime date = checkCreatedAt.get().getDate();
            Long hours = calculationHour(date);
            if(hours >= 24){
                return 0;
            }else{
                return 1;
            }
        }else{
            return 2;
        }
    }

    public Integer checkTime(Long roomId){
        Optional<Room> getRoom = roomRepository.findById(roomId);
        if(getRoom.isPresent()){
            Room room = getRoom.get();
            String status = room.getStatus();
            LocalDateTime date = room.getDate();
            Long hours = calculationHour(date);
            if(status.equals("0")){ // 7days room check
                if(hours >= 168){return 7;}
            }else{ // 1day room check
                if(hours >= 24){return 1;}
            }
            return 0;
        }
        return 9;
    }

    @Transactional
    public void updateRoom(Long roomId, String status){
        LocalDateTime time = LocalDateTime.now();
        roomRepository.updateCreatedAtAndStatus(roomId, time, status);
    }

    @Scheduled(cron = "0 0 0,12 * * *") // Run at 00, 12 o'clock every day
    public void performScheduledTask() {
        log.info("00, 12 delete Room");
        List<Room> roomList0 = roomRepository.findByStatus("0");
        for(Room room : roomList0){
            LocalDateTime date = room.getDate();
            Long hours = calculationHour(date);
            if(hours > 168){
                log.info("delete Room '0' : roomId = {}", room.getId());
                roomInMemberRepository.deleteByRoom(room);
                roomRepository.deleteById(room.getId());
            }
        }
        List<Room> roomList1 = roomRepository.findByStatus("1");
        for(Room room : roomList1){
            LocalDateTime date = room.getDate();
            Long hours = calculationHour(date);
            if(hours > 24){
                log.info("delete Room '1' : roomId = {}", room.getId());
                roomInMemberRepository.deleteByRoom(room);
                roomRepository.deleteById(room.getId());
            }
        }

    }
}