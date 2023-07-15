package com.mjuAppSW.appName.domain.report;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportCategory {

    @Id @GeneratedValue
    @Column(name = "Report_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    //테스트 용도 생성자
    public ReportCategory(String name) {
        this.name = name;
    }
}
