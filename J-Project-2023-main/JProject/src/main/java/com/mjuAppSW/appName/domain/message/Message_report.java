package com.mjuAppSW.appName.domain.message;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name="Message_report")
public class Message_report {
    @Id
    @Column(name="Report_id", nullable = false)
    private int reportId;

    @OneToOne
    @JoinColumn(name="Message_id")
    private Message MessageId;

//    @ManyToOne
//    @JoinColumn(name="Category_id")
//    @Column(name="Category_id", nullable = false)
//    private Category_id CategoryId;

    @Column(name="Content")
    private String Content;

    @Column(name="Date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date Date;
}