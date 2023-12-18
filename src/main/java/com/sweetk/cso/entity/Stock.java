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
public class Stock {  // 별도로 명시하지 않으면 이것이 테이블 명이다.

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long stoNo;

    @Column(nullable = false, unique = true)
    private String proCd;

    @Column(nullable = false, unique = false)
    private String inOut;

    @Column(nullable = false, unique = false)
    private long ioCnt;

    @Column(nullable = false, unique = false)
    private String outWy;

    @Column(nullable = false, unique = false)
    private String csmCd;

    @Column(nullable = false, unique = false)
    private String memo;

    @Column(nullable = false, unique = false)
    private String RegId;

    @Column(nullable = false, unique = false)
    private String RegDt;

    @Column(nullable = false, unique = false)
    private String ModId;

    @Column(nullable = false, unique = false)
    private String ModDt;
}
