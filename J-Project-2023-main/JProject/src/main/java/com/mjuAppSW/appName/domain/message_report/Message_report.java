package com.mjuAppSW.appName.domain.message_report;

import com.mjuAppSW.appName.domain.message.Message;
import com.mjuAppSW.appName.domain.report_category.Report_Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name="Message_report")
public class Message_report {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int reportId;

    @OneToOne
    @JoinColumn(name="messageId")
    @Column(nullable = false)
    private Message messageId;

    @ManyToOne
    @JoinColumn(name="report_id")
    @Column(nullable = false)
    private Report_Category categoryId;

    private String content;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public Message_report(Message message_id, Report_Category category_id, String content, Date date) {
        this.messageId = message_id;
        this.categoryId = category_id;
        this.content = content;
        this.date = date;
    }
}