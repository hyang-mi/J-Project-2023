package com.mjuAppSW.joA.domain.roomInMember;

import com.mjuAppSW.joA.domain.member.Member;
import com.mjuAppSW.joA.domain.member.MemberRepository;
import com.mjuAppSW.joA.domain.message.MessageRepository;
import com.mjuAppSW.joA.domain.room.Room;
import com.mjuAppSW.joA.domain.room.RoomRepository;
import com.mjuAppSW.joA.domain.room.RoomService;
import com.mjuAppSW.joA.domain.roomInMember.dto.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RoomInMemberService {
    private RoomInMemberRepository roomInMemberRepository;
    private RoomService roomService;
    private RoomRepository roomRepository;
    private MemberRepository memberRepository;
    private MessageRepository messageRepository;

    @Autowired
    public RoomInMemberService(RoomInMemberRepository roomInMemberRepository, RoomService roomService,
                               MemberRepository memberRepository, RoomRepository roomRepository,
                               MessageRepository messageRepository){
        this.roomInMemberRepository = roomInMemberRepository;
        this.roomService = roomService;
        this.memberRepository = memberRepository;
        this.roomRepository = roomRepository;
        this.messageRepository = messageRepository;
    }

    public Boolean findByRoomIdAndMemberId(Long roomId, Long memberId){
        Optional<Room> getRoom = roomRepository.findById(roomId);
        Optional<Member> getMember = memberRepository.findById(memberId);
        if(getRoom.isPresent() && getMember.isPresent()) {
            Room room = getRoom.get();
            Member member = getMember.get();
            Optional<RoomInMember> room_in_member = Optional.ofNullable(roomInMemberRepository.findByRoomAndMember(room, member));
            if(room_in_member.isPresent()){
                String status = room_in_member.get().getExpired();
                if(status == "0"){return true;}
            }else{return true;}
        }
        return false;
    }

    public Boolean findByRoom(Long roomId){
        Optional<Room> getRoom = roomRepository.findById(roomId);
        if(getRoom.isPresent()){
            Room room = getRoom.get();
            List<RoomInMember> roomInMemberList = roomInMemberRepository.findAllRoom(room);
            if(roomInMemberList.isEmpty() || roomInMemberList == null){
                return true;
            }
            for(RoomInMember roomInMember : roomInMemberList){
                if(roomInMember.getExpired().equals("0")){
                    return true;
                }
            }
        }
        return false;
    }

    public RoomList getRoomList(Long memberId){
        Optional<Member> getMember = memberRepository.findById(memberId);
        if(getMember.isPresent()){
            Member member = getMember.get();
            List<RoomInMember> memberList = roomInMemberRepository.findByAllMember(member);
            List<RoomDTO> roomDTOList = new ArrayList<>();
            if(memberList == null || memberList.isEmpty()){
                return new RoomList(null, "1");
            }else{
                for(RoomInMember rim : memberList){
                    List<RoomInMember> list = roomInMemberRepository.findByAllRoom(rim.getRoom());
                    for(RoomInMember rim1 : list) {
                        if (rim1.getMember() != member) {
                            RoomListResponse rlr = roomInMemberRepository.findRoomValue(rim1.getMember(), rim1.getRoom());
                            Integer unCheckedMessage = messageRepository.countUnCheckedMessage(rim1.getRoom(), rim1.getMember());
                            if(rlr != null){
                                RoomDTO roomDTO = new RoomDTO(rlr.getRoom().getId(), rlr.getName(), rlr.getUrlCode(), rlr.getContent(), String.valueOf(unCheckedMessage));
                                roomDTOList.add(roomDTO);
                            }
                        }
                    }
                }
            }
            return new RoomList(roomDTOList, "0");
        }
        return new RoomList(null, "2");
    }

    public RoomDTO getUpdateRoom(Room room, Member member){
        Optional<RoomInMember> roomInMember = Optional.ofNullable(roomInMemberRepository.findByRoomAndMember(room, member));
        if(roomInMember.isPresent()){
            RoomInMember rim = roomInMember.get();
            RoomListResponse rlr = roomInMemberRepository.findRoomValue(rim.getMember(), rim.getRoom());
            Integer unCheckedMessage = messageRepository.countUnCheckedMessage(rim.getRoom(), rim.getMember());
            if(rlr != null){
                RoomDTO roomDTO = new RoomDTO(rlr.getRoom().getId(), rlr.getName(), rlr.getUrlCode(), rlr.getContent(), String.valueOf(unCheckedMessage));
                return roomDTO;
            }
        }
        return null;
    }

    @Transactional
    public void createRoom(Long roomId, Long memberId){
        Optional<Room> getRoom = roomRepository.findById(roomId);
        Optional<Member> getMember = memberRepository.findById(memberId);
        if(getRoom.isPresent() && getMember.isPresent()){
            Room room = getRoom.get();
            Member member = getMember.get();
            RoomInMember rim = new RoomInMember(room, member, "1", "1");
            roomInMemberRepository.save(rim);
        }
    }

    public VoteDTO saveVoteResult(Long roomId, Long memberId, String result){
        Optional<Room> getRoom = roomRepository.findById(roomId);
        Optional<Member> getMember = memberRepository.findById(memberId);
        if(getRoom.isPresent() && getMember.isPresent()) {
            Room room = getRoom.get();
            Member member = getMember.get();
            roomInMemberRepository.saveVote(room, member, result);
            List<RoomInMember> getRoomInMember = roomInMemberRepository.findAllRoom(room);
            for(RoomInMember roomInMember : getRoomInMember){
                if(roomInMember.getMember() != member){
                    VoteResponse voteResponse = new VoteResponse(roomInMember.getRoom(), roomInMember.getMember(), roomInMember.getResult());
                    return new VoteDTO(voteResponse.getRoom().getId(), voteResponse.getMember().getId(), voteResponse.getResult());
                }
            }
        }
        return null;
    }

    public Boolean checkRoomIdAndMemberId(Long roomId, Long memberId){
        Optional<Room> getRoom = roomRepository.findById(roomId);
        Optional<Member> getMember = memberRepository.findById(memberId);
        if(getRoom.isPresent() && getMember.isPresent()) {
            Room room = getRoom.get();
            Member member = getMember.get();
            Optional<RoomInMember> check = Optional.ofNullable(roomInMemberRepository.findByRoomAndMember(room, member));
            if(check.isPresent()){
                return true;
            }else{return false;}
        }else{return false;}
    }

    public Boolean checkRoomInMember(Long memberId1, Long memberId2){
        Optional<Member> getMember1 = memberRepository.findById(memberId1);
        Optional<Member> getMember2 = memberRepository.findById(memberId2);
        if(getMember1.isPresent() && getMember2.isPresent()){
            Member member1 = getMember1.get();
            Member member2 = getMember2.get();
            List<RoomInMember> getRoomInMembers = roomInMemberRepository.checkRoomInMember(member1, member2);
            if(getRoomInMembers.isEmpty() || getRoomInMembers == null){
                return true;
            }
            for(RoomInMember rim : getRoomInMembers){
                if(rim.getExpired().equals("0")){
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public Boolean checkRoomId(Long roomId) {
        Optional<Room> getRoom = roomRepository.findById(roomId);
        if(getRoom.isPresent()){
            Room room = getRoom.get();
            List<RoomInMember> rimList = roomInMemberRepository.findAllRoom(room);
            if(rimList.isEmpty() || rimList == null){
                return false;
            }
            return true;
        }else{return false;}
    }

    public List<CheckVoteDTO> checkVoteResult(Long roomId){
        Optional<Room> getRoom = roomRepository.findById(roomId);
        if(getRoom.isPresent()){
            Room room = getRoom.get();
            List<RoomInMember> list = roomInMemberRepository.findAllRoom(room);
            List<CheckVoteDTO> response = new ArrayList<>();
            for(RoomInMember rim : list){
                CheckVoteDTO checkVoteDTO = new CheckVoteDTO(rim.getRoom().getId(), rim.getMember().getId(), rim.getResult());
                response.add(checkVoteDTO);
            }
            return response;
        }else{return new ArrayList<>();}
    }

    public UserInfoDTO getUserInfo(Long roomId, Long memberId){
        Optional<Room> getRoom = roomRepository.findById(roomId);
        Optional<Member> getMember = memberRepository.findById(memberId);
        if(getRoom.isEmpty() || getMember.isEmpty()){
            return new UserInfoDTO(null, null, null) ;
        }else if(getRoom.isPresent() && getMember.isPresent()){
            Room room = getRoom.get();
            Member member = getMember.get();
            Optional<RoomInMember> getRoomInMember = Optional.ofNullable(roomInMemberRepository.findByRoomAndMember(room, member));
            if(getRoomInMember.isPresent()){
                UserInfoResponse uir = roomInMemberRepository.getUserInfo(room, member);
                return new UserInfoDTO(uir.getName(), uir.getUrlCode(), uir.getBio());
            }
            return new UserInfoDTO(null, null, null) ;
        }
        return new UserInfoDTO(null, null, null) ;
    }

    @Transactional
    public Boolean updateExpired(Long roomId, Long memberId, String expired) {
        Optional<Room> getRoom = roomRepository.findById(roomId);
        Optional<Member> getMember = memberRepository.findById(memberId);
        if (getRoom.isPresent() && getMember.isPresent()) {
            Room room = getRoom.get();
            Member member = getMember.get();
            roomInMemberRepository.updateExpired(room, member, expired);
            return true;
        }
        return false;
    }

    public Boolean updateEntryTime(String sRoomId, String sMemberId){
        Long getRoomId = Long.parseLong(sRoomId);
        Long getMemberId = Long.parseLong(sMemberId);
        Optional<Room> getRoom = roomRepository.findById(getRoomId);
        Optional<Member> getMember = memberRepository.findById(getMemberId);
        if (getRoom.isPresent() && getMember.isPresent()) {
            Room room = getRoom.get();
            Member member = getMember.get();
            LocalDateTime currentDateTime = LocalDateTime.now();
            roomInMemberRepository.updateEntryTime(room, member, currentDateTime);
            return true;
        }
        return false;
    }

    public Boolean updateExitTime(String sRoomId, String sMemberId){
        Long getRoomId = Long.parseLong(sRoomId);
        Long getMemberId = Long.parseLong(sMemberId);
        Optional<Room> getRoom = roomRepository.findById(getRoomId);
        Optional<Member> getMember = memberRepository.findById(getMemberId);
        if (getRoom.isPresent() && getMember.isPresent()) {
            Room room = getRoom.get();
            Member member = getMember.get();
            LocalDateTime currentDateTime = LocalDateTime.now();
            roomInMemberRepository.updateExitTime(room, member, currentDateTime);
            return true;
        }
        return false;
    }

    public Boolean checkExpired(Long roomId, Long memberId){
        Optional<Room> getRoom = roomRepository.findById(roomId);
        Optional<Member> getMember = memberRepository.findById(memberId);
        if (getRoom.isPresent() && getMember.isPresent()) {
            Room room = getRoom.get();
            Member member = getMember.get();
            RoomInMember getRim = roomInMemberRepository.checkExpired(room, member);
            if(getRim.getExpired().equals("1")){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }
}