package com.mjuAppSW.appName.domain.message_report;


import com.mjuAppSW.appName.domain.message.Message;
import com.mjuAppSW.appName.domain.report_category.Report_Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class Message_report_Service {

    @Autowired
    private Message_report_Repository message_report_repository;

    void messageReport(Message message_id, Report_Category category_id, String content){
        Message_report Message_report = new Message_report(message_id, category_id, content, new Date());
        message_report_repository.save(Message_report);
    }
}
