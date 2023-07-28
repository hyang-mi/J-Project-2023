package com.mjuAppSW.appName.domain.message_report;

import com.mjuAppSW.appName.domain.message.Message;
import com.mjuAppSW.appName.domain.report_category.Report_Category;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.Date;

@SpringBootTest
class Message_report_ServiceTest {

    @Autowired
    private Message_report_Repository message_report_repository;

    @Test
    @Transactional
    @Rollback(false)
    void messageReport() {
        Message message = new Message(1L);
        Report_Category report_category = new Report_Category(1L);
        String content = new String("너 신고!");
        Date date = new Date();
        Message_report Message_report = new Message_report(message, report_category, content, date);
        message_report_repository.save(Message_report);
    }
}