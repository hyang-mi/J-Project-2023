package com.mjuAppSW.joA.domain.roomInMember;

import com.mjuAppSW.joA.domain.member.Member;
import com.mjuAppSW.joA.domain.member.MemberRepository;
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

    @Autowired
    public RoomInMemberService(RoomInMemberRepository roomInMemberRepository, RoomService roomService,
                               MemberRepository memberRepository, RoomRepository roomRepository){
        this.roomInMemberRepository = roomInMemberRepository;
        this.roomService = roomService;
        this.memberRepository = memberRepository;
        this.roomRepository = roomRepository;
    }

    public boolean findByRoomIdAndMemberId(Long roomId, Long memberId){
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

    public RoomList getRoomList(Long memberId, String expired){ // 프로필 사진도 같이
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
                            RoomListResponse rlr = roomInMemberRepository.findByMemberIdAndExpired(rim1.getMember(), rim1.getRoom(), expired);
                            if(rlr != null){
                                RoomDTO roomDTO = new RoomDTO(rlr.getRoom().getRoomId(), rlr.getName(), rlr.getUrlCode(), rlr.getContent());
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


//    public List<RoomListDTO> getRoomList(long member, char expired){
//        Member memberObj = new Member(member);
//        List<Room_in_member> memberList = room_in_member_repository.findByAllMember(memberObj);
//        RoomListDTO roomListDTO = new RoomListDTO();
//        List<RoomListDTO> roomListDTOList = new ArrayList<>();
//        if(memberList == null || memberList.isEmpty()){
//            return null;
//        }else{
//            for(Room_in_member rim : memberList){
//                List<Room_in_member> list = room_in_member_repository.findByAllRoom(rim.getRoomId());
//                for(Room_in_member rim1 : list) {
//                    if (rim1.getMemberId().getId() != memberObj.getId()) {
//                        RoomListResponse rlr = room_in_member_repository.findByMemberIdAndExpired(rim1.getMemberId().getId(), expired);
//                        roomListDTO.setRoomId(rlr.getRoom().getRoomId());
//                        roomListDTO.setName(rlr.getName());
//                        roomListDTO.setImagePath(rlr.getImagePath());
//                        roomListDTO.setContent(rlr.getContent());
//                        roomListDTOList.add(roomListDTO);
//                        roomListDTO = new RoomListDTO();
//                    }
//                }
//            }
//        }
//        return roomListDTOList;
//    }

    @Transactional
    public void createRoom(Long roomId, Long memberId){
        log.info("webSocket createRoom");
        log.info("Room : " + roomId);
        log.info("Member : " + memberId);
        Optional<Room> getRoom = roomRepository.findById(roomId);
        Optional<Member> getMember = memberRepository.findById(memberId);
        if(getRoom.isPresent() && getMember.isPresent()){
            Room room = getRoom.get();
            Member member = getMember.get();
            RoomInMember rim = new RoomInMember(room, member, "1", "2");
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
                    return new VoteDTO(voteResponse.getRoom().getRoomId(), voteResponse.getMember().getId(), voteResponse.getResult());
                }
            }
        }
        return null;
    }

    public boolean checkRoomIdAndMemberId(Long roomId, Long memberId){
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

    public boolean checkRoomId(Long roomId) {
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
                CheckVoteDTO checkVoteDTO = new CheckVoteDTO(rim.getRoom().getRoomId(), rim.getMember().getId(), rim.getResult());
                response.add(checkVoteDTO);
            }
            return response;
        }else{return new ArrayList<>();}
    }

    @Transactional
    public boolean updateExpired(Long roomId, Long memberId, String expired) {
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
}