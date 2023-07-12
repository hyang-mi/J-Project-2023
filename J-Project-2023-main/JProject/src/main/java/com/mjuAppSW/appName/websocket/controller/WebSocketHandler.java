package com.mjuAppSW.appName.websocket.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mjuAppSW.appName.domain.member.Member;
import com.mjuAppSW.appName.domain.room.Room;
import com.mjuAppSW.appName.domain.room.RoomService;
import com.mjuAppSW.appName.domain.room_in_member.Room_in_member_Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
    @Autowired
    private Room_in_member_Service room_in_member_service;
    @Autowired
    private RoomService roomService;
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException{
        String payload = message.getPayload();
        log.info("payload : {}", payload);
        String[] arr = payload.split(" ");
        // 받는 형태, String 형태로, R RoomId MemberId MemberId, M RoomId MemberId Content
        String check = arr[0];

        if(check.equals("R")) { // Room_in_member에 관한 로직
            String roomId = arr[1];
            String memberId1 = arr[2];
            String memberId2 = arr[3];

            Room room = new Room();
            room.setRoomId(Long.parseLong(roomId));

            Member member1 = new Member();
            member1.setId(Long.parseLong(memberId1));
            Member member2 = new Member();
            member2.setId(Long.parseLong(memberId2));

            boolean checkMember1 = room_in_member_service.findByRoomIdAndMemberId(room, member1);
            boolean checkMember2 = room_in_member_service.findByRoomIdAndMemberId(room, member2);

            if(checkMember1 && checkMember2){
                room_in_member_service.createRoom(room, member1);
                room_in_member_service.createRoom(room, member2);
            }

        }else if(check.equals("M")){ // Message에 관한 로직
            String roomId = arr[1];
            String memberId1 = arr[2];
            String content = arr[3];
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception{
        sessions.add(session);

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception{

    }

}
