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
public class Sales {  // 별도로 명시하지 않으면 이것이 테이블 명이다.

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long salesNo;

    @Column(nullable = false, unique = false)
    private long stoNo;

    @Column(nullable = false, unique = true)
    private String proCd;

    @Column(nullable = false, unique = true)
    private String lotNo;

    @Column(nullable = false, unique = true)
    private String expDt;

    @Column(nullable = false, unique = false)
    private long outCnt;

    @Column(nullable = false, unique = false)
    private String fromStorage;

    @Column(nullable = false, unique = false)
    private String outWy;

    @Column(nullable = false, unique = false)
    private String csmCd;

    @Column(nullable = false, unique = false)
    private String memo;

    @Column(nullable = false, unique = false)
    private String regId;

    @Column(nullable = false, unique = false)
    private String regDt;

    @Column(nullable = false, unique = false)
    private String modId;

    @Column(nullable = false, unique = false)
    private String modDt;
}
