package com.mjuAppSW.joA.domain.report;

import com.mjuAppSW.joA.domain.report.dto.ReportRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class MessageReportApiController {

    private MessageReportService messageReportService;

    @Autowired
    public MessageReportApiController(MessageReportService messageReportService){
        this.messageReportService = messageReportService;
    }

    @PostMapping("/report/message")
    public HttpStatus messageReport(@RequestBody ReportRequest request){
        log.info("messageReport");
        log.info("messageId : " + request.getMessageId());
        log.info("categoryId : " + request.getCategoryId());
        log.info("content : " + request.getContent());
        boolean save = messageReportService.messageReport(request.getMessageId(), request.getCategoryId(), request.getContent());
        if(save){
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST; // messageId or CategoryId is wrong
    }
}
