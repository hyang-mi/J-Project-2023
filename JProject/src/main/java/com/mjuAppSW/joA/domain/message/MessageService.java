package com.mjuAppSW.joA.domain.message;

import com.mjuAppSW.joA.domain.member.Member;
import com.mjuAppSW.joA.domain.member.MemberRepository;
import com.mjuAppSW.joA.domain.message.dto.MessageList;
import com.mjuAppSW.joA.domain.message.dto.MessageResponse;
import com.mjuAppSW.joA.domain.room.Room;
import com.mjuAppSW.joA.domain.room.RoomRepository;
import com.mjuAppSW.joA.domain.roomInMember.RoomInMember;
import com.mjuAppSW.joA.domain.roomInMember.RoomInMemberRepository;
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
    private RoomInMemberRepository roomInMemberRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, RoomRepository roomRepository,
                          MemberRepository memberRepository, RoomInMemberRepository roomInMemberRepository){
        this.messageRepository = messageRepository;
        this.roomRepository = roomRepository;
        this.memberRepository = memberRepository;
        this.roomInMemberRepository = roomInMemberRepository;
    }

    public Boolean saveMessage(Long roomId, Long memberId, String content, String isChecked){
        Optional<Room> getRoom = roomRepository.findById(roomId);
        Optional<Member> getMember = memberRepository.findById(memberId);
        if(getRoom.isPresent() && getMember.isPresent()){
            Room room = getRoom.get();
            Member member = getMember.get();
            Message message = new Message(member, room, content, new Date(), isChecked);
            messageRepository.save(message);
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
            Optional<RoomInMember> getRoomInMember = Optional.ofNullable(roomInMemberRepository.findByRoomAndMember(room, member));
            if(getRoomInMember.isEmpty()){
                return new MessageList(new ArrayList<>(), "2");
            }
            List<Message> messageList = messageRepository.findByRoom(room);
            if(messageList.isEmpty() || messageList == null){
                return new MessageList(new ArrayList<>(), "1");
            }
            List<MessageResponse> messageResponseList = new ArrayList<>();
            for(Message message : messageList){
                String getMessage = "";
                if(message.getMember() == member){
                    getMessage = "R " + message.getContent();
                }else {
                    getMessage = "L " + message.getContent();
                }
                MessageResponse messageResponse = new MessageResponse(getMessage);
                messageResponseList.add(messageResponse);
            }
            return new MessageList(messageResponseList, "0");
        }
        return new MessageList(new ArrayList<>(), "3");
    }

    public Boolean updateIsChecked(String roomId, String memberId){
        Optional<Room> getRoom = roomRepository.findById(Long.parseLong(roomId));
        Optional<Member> getMember = memberRepository.findById(Long.parseLong(memberId));
        if(getRoom.isPresent() && getMember.isPresent()){
            Room room = getRoom.get();
            Member member = getMember.get();
            List<Message> getMessages = messageRepository.findMessage(room, member);
            if(getMessages.isEmpty() || getMessages == null){
                return true;
            }
            messageRepository.updateIsChecked(getMessages);
            return true;
        }
        return false;
    }
}