package com.mjuAppSW.appName.domain.message_report.dto;

import com.mjuAppSW.appName.domain.message.Message;
import com.mjuAppSW.appName.domain.report_category.Report_Category;
import lombok.Data;

@Data
public class ReportRequest {
    private Message message_id;
    private Report_Category category_id;
    private String content;
}
