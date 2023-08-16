package com.mjuAppSW.joA.domain.report;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="Report_Category")
public class ReportCategory {

    @Id @GeneratedValue
    @Column(name = "Report_id")
    public Long id;

    @Column(nullable = false)
    private String name;

    //테스트 용도 생성자
    public ReportCategory(String name) {
        this.name = name;
    }
}
