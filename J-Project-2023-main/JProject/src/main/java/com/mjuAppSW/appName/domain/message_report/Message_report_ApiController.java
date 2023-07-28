package com.mjuAppSW.appName.domain.message_report;


import com.mjuAppSW.appName.domain.message_report.dto.ReportRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Message_report")
public class Message_report_ApiController {

    @Autowired
    private Message_report_Service message_report_service;

    @PostMapping("/reportMessage_report")
    public HttpStatus messageReport(@RequestBody ReportRequest reportRequest){
        message_report_service.messageReport(reportRequest.getMessage_id(), reportRequest.getCategory_id(), reportRequest.getContent());
        return HttpStatus.OK;
    }
}
