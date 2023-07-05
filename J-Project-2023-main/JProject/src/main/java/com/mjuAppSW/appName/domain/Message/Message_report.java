package com.mjuAppSW.appName.domain.Message;

import com.mjuAppSW.appName.domain.Message.Message;
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
    private int Report_id;

    @OneToOne
    @JoinColumn(name="Message_id")
    @Column(name="Message_id", nullable = false)
    private Message Message_id;

    @ManyToOne
    @JoinColumn(name="Category_id")
    @Column(name="Category_id", nullable = false)
    private Category_id Category_id;

    @Column(name="Content")
    private String Content;

    @Column(name="Date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date Date;
}