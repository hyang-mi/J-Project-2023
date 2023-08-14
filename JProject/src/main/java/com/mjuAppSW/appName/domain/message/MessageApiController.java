package com.mjuAppSW.appName.domain.message;

import com.mjuAppSW.appName.domain.message.dto.MessageList;
import com.mjuAppSW.appName.domain.message.dto.MessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class MessageApiController {
    private MessageService messageService;

    @Autowired
    public MessageApiController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/load/message")
    public ResponseEntity<List<MessageResponse>> loadMessage(
            @RequestParam("roomId") Long roomId, @RequestParam("memberId") Long memberId) {
        log.info("loadMessage");
        log.info("roomId : " + roomId);
        log.info("memberId : " + memberId);
        MessageList list = messageService.loadMessage(roomId, memberId);
        if (list.getStatus().equals("0") || list.getStatus().equals("1")) {
            return ResponseEntity.ok(list.getMessageResponseList());
        } else if (list.getStatus().equals("2")) {
            return ResponseEntity.notFound().build(); // roomInMember에 해당하는 방이 없을 때
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}