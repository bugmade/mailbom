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
public class Consumer {  // 별도로 명시하지 않으면 이것이 테이블 명이다.

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long csmNo;

    @Column(nullable = false, unique = true)
    private String csmCd;

    @Column(nullable = false, unique = false)
    private String csmNm;

    @Column(nullable = false, unique = false)
    private String bizNo;

    @Column(nullable = false, unique = false)
    private String addr;

    @Column(nullable = false, unique = false)
    private String picNm;

    @Column(nullable = false, unique = false)
    private String telNo;

    @Column(nullable = false, unique = false)
    private String email;

    @Column(nullable = false, unique = false)
    private String csmDt;

    @Column(nullable = false, unique = false)
    private String RegId;

    @Column(nullable = false, unique = false)
    private String RegDt;

    @Column(nullable = false, unique = false)
    private String ModId;

    @Column(nullable = false, unique = false)
    private String ModDt;
}
