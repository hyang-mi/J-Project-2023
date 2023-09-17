package com.mjuAppSW.joA.domain.report.message;


import com.mjuAppSW.joA.domain.message.Message;
import com.mjuAppSW.joA.domain.message.MessageRepository;
import com.mjuAppSW.joA.domain.report.ReportCategory;
import com.mjuAppSW.joA.domain.report.ReportCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class MessageReportService {

    private MessageReportRepository messageReportRepository;
    private MessageRepository messageRepository;
    private ReportCategoryRepository reportCategoryRepository;

    @Autowired
    public MessageReportService(MessageReportRepository message_report_repository, MessageRepository messageRepository,
                                ReportCategoryRepository reportCategoryRepository){
        this.messageReportRepository = message_report_repository;
        this.messageRepository = messageRepository;
        this.reportCategoryRepository = reportCategoryRepository;
    }

    public boolean messageReport(Long messageId, Long categoryId, String content){
        Optional<Message> getMessage = messageRepository.findById(messageId);
        Optional<ReportCategory> getReportCategory = reportCategoryRepository.findById(categoryId);
        if(getMessage.isPresent() && getReportCategory.isPresent()){
            Message message = getMessage.get();
            ReportCategory category = getReportCategory.get();
            MessageReport MessageReport = new MessageReport(message, category, content, new Date());
            messageReportRepository.save(MessageReport);
            return true;
        }
        return false;
    }
}