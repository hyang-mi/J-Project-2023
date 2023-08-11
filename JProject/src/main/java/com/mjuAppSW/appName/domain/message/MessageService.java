package com.mjuAppSW.appName.domain.message;

import com.mjuAppSW.appName.domain.member.Member;
import com.mjuAppSW.appName.domain.member.MemberRepository;
import com.mjuAppSW.appName.domain.message.dto.MessageList;
import com.mjuAppSW.appName.domain.message.dto.MessageResponse;
import com.mjuAppSW.appName.domain.room.Room;
import com.mjuAppSW.appName.domain.room.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private MessageRepository messageRepository;
    private RoomRepository roomRepository;
    private MemberRepository memberRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, RoomRepository roomRepository,
                          MemberRepository memberRepository){
        this.messageRepository = messageRepository;
        this.roomRepository = roomRepository;
        this.memberRepository = memberRepository;
    }

    public boolean saveMessage(Long roomId, Long memberId, String content){
        Optional<Room> getRoom = roomRepository.findById(roomId);
        Optional<Member> getMember = memberRepository.findById(memberId);
        if(getRoom.isPresent() && getMember.isPresent()){
            Room room = getRoom.get();
            Member member = getMember.get();
            Message message = new Message(member, room, content, new Date(), "1");
            messageRepository.save(message);
            return true;
        }
        return false;
    }

    public boolean checkRoom(Long roomId){
        Optional<Room> getRoom = roomRepository.findById(roomId);
        if(getRoom.isPresent()){
            Room room = getRoom.get();
            List<Message> messageList = messageRepository.findByRoom(room);
            if(messageList == null || messageList.isEmpty()){
                return false;
            }
            return true;
        }
        return false;
    }

    public MessageList loadMessage(Long roomId, Long memberId){
        Optional<Room> getRoom = roomRepository.findById(roomId);
        Optional<Member> getMember = memberRepository.findById(memberId);
        if(getRoom.isPresent() && getMember.isPresent()) {
            Room room = getRoom.get();
            Member member = getMember.get();
            List<Message> messageList = messageRepository.findByRoom(room);
            if(messageList.isEmpty() || messageList == null){
                return new MessageList(null, "1");
            }
            List<MessageResponse> messageResponseList = new ArrayList<>();
            for(Message message : messageList){
                String getMessage = "";
                if(message.getMember() == member){
                    getMessage = "L " + message.getContent();
                }else {
                    getMessage = "R " + message.getContent();
                }
                MessageResponse messageResponse = new MessageResponse(getMessage);
                messageResponseList.add(messageResponse);
            }
            return new MessageList(messageResponseList, "0");
        }
        return new MessageList(null, "2");
    }

//    public Boolean updateIsChecked(Long roomId, Long memberId, String isChecked){
//        Optional<Room> getRoom = roomRepository.findById(roomId);
//        Optional<Member> getMember = memberRepository.findById(memberId);
//        if(getRoom.isPresent() && getMember.isPresent()){
//            Room room = getRoom.get();
//            Member member = getMember.get();
//            messageRepository.updateIsChecked(room, member, isChecked);
//            return true;
//        }
//        return false;
//    }
}
