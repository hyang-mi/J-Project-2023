package com.mjuAppSW.joA.domain.voteCategory;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoteCategory {

    @Id @GeneratedValue
    @Column(name = "Category_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    //테스트 용도 생성자
    public VoteCategory(String name) {
        this.name = name;
    }
}
