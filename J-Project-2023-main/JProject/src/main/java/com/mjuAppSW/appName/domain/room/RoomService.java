package com.mjuAppSW.appName.domain.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    public void createRoom(){
        Room room = Room.createRoomItem(new Date(), '1');
        roomRepository.save(room);
    }

    public boolean checkRoomId(long roomId) {
        Optional<Room> findRoomId = roomRepository.findById(roomId);
        if(findRoomId.isPresent()){
            return true;
        }else {
            return false;
        }
    }

    public int checkCreateAtRoom(long roomId){
        Optional<Room> checkCreatedAt = Optional.ofNullable(roomRepository.findByDate(roomId));
        if(checkCreatedAt.isPresent()){
            Date date = checkCreatedAt.get().getDate();
            Date currentDate = new Date();
            long getTime = currentDate.getTime() - date.getTime();
            long getHour = TimeUnit.MILLISECONDS.toHours(getTime);
            if(getHour >= 24){
                return 0;
            }else{
                return 1;
            }
        }else{
            return 2;
        }
    }

    public void updateRoom(long roomId, Date date, char status){
        roomRepository.updateCreatedAtAndStatus(roomId, date, status);
    }
}