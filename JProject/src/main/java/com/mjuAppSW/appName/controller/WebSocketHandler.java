package com.mjuAppSW.appName.controller;

import com.mjuAppSW.appName.domain.member.MemberRepository;
import com.mjuAppSW.appName.domain.message.MessageService;
import com.mjuAppSW.appName.domain.room.RoomRepository;
import com.mjuAppSW.appName.domain.roomInMember.RoomInMemberService;
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
import java.util.*;
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

    private final static String isChecked = "0";

    @Autowired
    public WebSocketHandler(RoomInMemberService roomInMemberService, MessageService messageService,
                            RoomRepository roomRepository, MemberRepository memberRepository){
        this.roomInMemberService = roomInMemberService;
        this.messageService = messageService;
        this.roomRepository = roomRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException{
        String payload = message.getPayload();
        log.info("payload : {}", payload);
        String[] arr = payload.split(" ", 4);
        // 받는 형태, String 형태로, R RoomId MemberId MemberId, M RoomId MemberId Content
        String check = arr[0];
        Long roomId = Long.parseLong(arr[1]);
        if(check.equals("R")) { // Room_in_member에 관한 로직
            Long memberId1 = Long.parseLong(arr[2]);
            Long memberId2 = Long.parseLong(arr[3]);

            boolean checkMemberAndRoom1 = roomInMemberService.findByRoomIdAndMemberId(roomId, memberId1);
            boolean checkMemberAndRoom2 = roomInMemberService.findByRoomIdAndMemberId(roomId, memberId2);

            if(checkMemberAndRoom1 && checkMemberAndRoom2){
                roomInMemberService.createRoom(roomId, memberId1);
                roomInMemberService.createRoom(roomId, memberId2);
            }
        }else if(check.equals("M")){ // Message에 관한 로직
            Long memberId = Long.parseLong(arr[2]);
            String content = arr[3];

            boolean save = messageService.saveMessage(roomId, memberId, content);
            if(save){
                System.out.println("Save message : " + content);
            }else{
                System.out.println("roomId or memberId is wrong");
            }

//            messageService.updateIsChecked(roomId, memberId, isChecked);

            String stringRoomId = String.valueOf(roomId);

            List<WebSocketSession> roomSessionsList = roomSessions.get(stringRoomId);
            if (roomSessionsList != null) {
                for (WebSocketSession targetSession : roomSessionsList) {
                    if (targetSession.isOpen() && !targetSession.equals(session)) {
                        targetSession.sendMessage(new TextMessage(content));
                    }
                }
            }
        }
    }

    private String getMemberId(WebSocketSession session) throws URISyntaxException {
        URI uri = new URI(session.getUri().toString());
        String query = uri.getQuery();
        String[] arr = query.split("&");
        String getString = arr[arr.length-1];
        String getMemberId = String.valueOf(getString.charAt(getString.length()-1));
        return getMemberId;
    }

    private String getRoomId(WebSocketSession session) throws URISyntaxException {
        URI uri = new URI(session.getUri().toString());
        String query = uri.getQuery();
        String[] arr = query.split("&");
        String getString = arr[arr.length-2];
        String getId = String.valueOf(getString.charAt(getString.length()-1));
        return getId;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception{
        System.out.println("websocket connect");
        String roomId = getRoomId(session);
        System.out.println("roomId : " + roomId);
        String memberId = getMemberId(session);
        System.out.println("memberId : " + memberId);
        roomSessions.computeIfAbsent(roomId, key -> new ArrayList<>()).add(session);


        Boolean updateEntryTime = roomInMemberService.updateEntryTime(roomId, memberId);
        if(updateEntryTime){
            System.out.println("update entry time");
        }else{System.out.println("roomId or memberId is wrong");}
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception{
        System.out.println("websocket closed");
        String roomId = getRoomId(session);
        System.out.println("roomId : " + roomId);
        String memberId = getMemberId(session);
        System.out.println("memberId : " + memberId);
        List<WebSocketSession> roomSessionsList = roomSessions.get(roomId);
        if (roomSessionsList != null) {
            roomSessionsList.remove(session);
            if (roomSessionsList.isEmpty()) {
                roomSessions.remove(roomId);
            }
        }
        Boolean updateExitTime = roomInMemberService.updateExitTime(roomId, memberId);
        if(updateExitTime){
            System.out.println("update exit time");
        }else{System.out.println("roomId or memberId is wrong");}
    }
}