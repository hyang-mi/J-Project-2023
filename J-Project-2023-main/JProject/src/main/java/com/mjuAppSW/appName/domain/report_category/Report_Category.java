package com.mjuAppSW.appName.domain.report_category;
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
public class Report_Category {

    @Id
    @Column(nullable = false)
    private Long report_id;
    @Column(nullable = false)
    private String name;

    //테스트 용도 생성자
//    public Report_Category(String name) {
//        this.name = name;
//    }

    public Report_Category(Long report_id){
        this.report_id = report_id;
    }
}