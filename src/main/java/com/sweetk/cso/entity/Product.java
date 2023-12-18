package com.sweetk.cso.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class Product {  // 별도로 명시하지 않으면 이것이 테이블 명이다.

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long proNo;

    @Column(nullable = false, unique = true)
    private String proCd;

    @Column(nullable = false, unique = false)
    private String proNm;

    @Column(nullable = false, unique = false)
    private String proDt;

    @Column(nullable = false, unique = false)
    private long proSt;

    @Column(nullable = false, unique = false)
    private String RegId;

    @Column(nullable = false, unique = false)
    private String RegDt;

    @Column(nullable = false, unique = false)
    private String ModId;

    @Column(nullable = false, unique = false)
    private String ModDt;
}
