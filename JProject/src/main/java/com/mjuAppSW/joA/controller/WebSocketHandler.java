package com.mjuAppSW.joA.controller;

import com.mjuAppSW.joA.domain.member.MemberRepository;
import com.mjuAppSW.joA.domain.message.MessageService;
import com.mjuAppSW.joA.domain.room.RoomRepository;
import com.mjuAppSW.joA.domain.room.RoomService;
import com.mjuAppSW.joA.domain.roomInMember.RoomInMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketHandler extends TextWebSocketHandler {
    private Map<String, List<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();
    private RoomInMemberService roomInMemberService;
    private MessageService messageService;
    private RoomRepository roomRepository;
    private MemberRepository memberRepository;
    private RoomService roomService;

    @Autowired
    public WebSocketHandler(RoomInMemberService roomInMemberService, MessageService messageService,
                            RoomRepository roomRepository, MemberRepository memberRepository, RoomService roomService){
        this.roomInMemberService = roomInMemberService;
        this.messageService = messageService;
        this.roomRepository = roomRepository;
        this.memberRepository = memberRepository;
        this.roomService = roomService;
    }
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload();
        log.info("payload : {}", payload);
        String[] arr = payload.split(" ", 4);

        String separator = arr[0];
        if(separator.equals("R")){
            makeRoom(Long.parseLong(arr[1]), Long.parseLong(arr[2]), Long.parseLong(arr[3]));
        }else if(separator.equals("M")){
            sendMessage(arr[1], arr[2], arr[3], session);
        }
    }

    public void makeRoom(Long roomId, Long memberId1, Long memberId2){
        Boolean checkMemberAndRoom1 = roomInMemberService.findByRoomIdAndMemberId(roomId, memberId1);
        Boolean checkMemberAndRoom2 = roomInMemberService.findByRoomIdAndMemberId(roomId, memberId2);

        if(checkMemberAndRoom1 || checkMemberAndRoom2){
            roomInMemberService.createRoom(roomId, memberId1);
            roomInMemberService.createRoom(roomId, memberId2);
            log.info("makeRoom : roomId = {}, memberId1 = {}", roomId, memberId1);
            log.info("makeRoom : roomId = {}, memberId2 = {}", roomId, memberId2);
        }
        log.warn("makeRoom : getValue's not correct or already exist");
        log.warn("makeRoom : roomId = {}, memberId1 = {}, memberId2 = {}", roomId, memberId1, memberId2);
    }

    public void sendMessage(String roomId, String memberId, String content,
                            WebSocketSession session) throws IOException {
        // check Expired
        Boolean checkExpired = roomInMemberService.checkExpired(Long.parseLong(roomId), Long.parseLong(memberId));
        // check createdAt
        Integer checkTime = roomService.checkTime(Long.parseLong(roomId));

        if(checkExpired && checkTime == 0){
            List<WebSocketSession> roomSessionsList = roomSessions.get(roomId);
            if(roomSessionsList != null){
                int count = 2;
                for(int i=0; i<roomSessionsList.size(); i++){
                    count--;
                }
                String isChecked = String.valueOf(count);
                Boolean save = messageService.saveMessage(Long.parseLong(roomId), Long.parseLong(memberId), content, isChecked);
                if(save){
                    log.info("SaveMessage : roomId = {}, memberId = {}, content = {}, isChecked = {}", roomId, memberId, content, isChecked);
                    for (WebSocketSession targetSession : roomSessionsList) {
                        if (targetSession.isOpen()  && !targetSession.equals(session)) {
                            log.info("sendMessage : roomId = {}, memberId = {}, content = {}", roomId, memberId, content);
                            targetSession.sendMessage(new TextMessage(content));
                        }
                    }
                }else{
                    log.warn("SaveMessage : getValue's not correct");
                    log.warn("SaveMessage : roomId = {}, memberId = {}", roomId, memberId);
                }
            }
        }else if(!checkExpired){
            log.info("checkExpired '0' : roomId = {}", roomId);
            String exitMessage = "상대방이 채팅방을 나갔습니다.";
            List<WebSocketSession> roomSessionsList = roomSessions.get(roomId);
            if (roomSessionsList != null) {
                for (WebSocketSession targetSession : roomSessionsList) {
                    if (targetSession.equals(session)) {
                        targetSession.sendMessage(new TextMessage(exitMessage));
                    }
                }
            }
        }else if(checkTime != 0){
            String alarmMessage = "";
            if(checkTime == 1){
                log.info("checkTime over 24hours : roomId = {}", roomId);
                alarmMessage = "방 유효시간이 지났기 때문에 메시지를 보낼 수 없습니다.";
            }else if(checkTime == 7){
                log.info("checkTime over 7days : roomId = {}", roomId);
                alarmMessage = "방 유효시간이 지났기 때문에 메시지를 보낼 수 없습니다.";
            }else{
                log.warn("checkTime : getValue's not correct");
                log.warn("checkTime : roomId = {}", roomId);
            }
            List<WebSocketSession> roomSessionsList = roomSessions.get(roomId);
            if (roomSessionsList != null) {
                for (WebSocketSession targetSession : roomSessionsList) {
                    if (targetSession.equals(session)) {
                        targetSession.sendMessage(new TextMessage(alarmMessage));
                    }
                }
            }
        }
    }

    private String getMemberId(WebSocketSession session) throws URISyntaxException {
        URI uri = new URI(session.getUri().toString());
        String query = uri.getQuery();
        if(query == null){
            return null;
        }
        String[] arr = query.split("&");
        String getString = arr[arr.length-1];
        String getMemberId = String.valueOf(getString.charAt(getString.length()-1));
        return getMemberId;
    }

    private String getRoomId(WebSocketSession session) throws URISyntaxException {
        URI uri = new URI(session.getUri().toString());
        String query = uri.getQuery();
        if(query == null){
            return null;
        }
        String[] arr = query.split("&");
        String getString = arr[arr.length-2];
        String getId = String.valueOf(getString.charAt(getString.length()-1));
        return getId;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception{
        String roomId = getRoomId(session);
        String memberId = getMemberId(session);
        log.info("websocketConnect : roomId = {}, memberId = {]", roomId, memberId);
        if(roomId == null && memberId == null){
        }else{
            roomSessions.computeIfAbsent(roomId, key -> new ArrayList<>()).add(session);

            Boolean updateEntryTime = roomInMemberService.updateEntryTime(roomId, memberId);
            if(updateEntryTime){
                log.info("updateEntryTime : roomId = {}, memberId = {}", roomId, memberId);
            }else{
                log.warn("updateEntryTime : getValue's not correct");
                log.warn("updateEntryTime : roomId = {}, memberId = {}", roomId, memberId);
            }

            Boolean updateIsChecked = messageService.updateIsChecked(roomId, memberId);
            if(updateIsChecked){
                log.info("updateIsChecked : roomId = {}, memberId = {}", roomId, memberId);
            }else{
                log.warn("updateIsChecked : getValue's not correct");
                log.warn("updateIsChecked : roomId = {}, memberId = {}", roomId, memberId);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String roomId = getRoomId(session);
        String memberId = getMemberId(session);
        log.info("websocketClosed : roomId = {}, memberId = {}", roomId, memberId);
        if(roomId == null && memberId == null){
        } else {
            List<WebSocketSession> roomSessionsList = roomSessions.get(roomId);
            if (roomSessionsList != null) {
                roomSessionsList.remove(session);
                if (roomSessionsList.isEmpty()) {
                    log.info("websocketClosed Remove : roomId = {}", roomId);
                    roomSessions.remove(roomId);
                }
            }
            Boolean updateExitTime = roomInMemberService.updateExitTime(roomId, memberId);
            if (updateExitTime) {
                log.info("updateExitTime : roomId = {}, memberId = {}", roomId, memberId);
            } else {
                log.warn("updateExitTime : getValue's not correct");
                log.warn("updateExitTime : roomId = {}, memberId = {}", roomId, memberId);
            }
        }
    }
}
