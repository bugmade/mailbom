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
public class WprIo {  // 별도로 명시하지 않으면 이것이 테이블 명이다.

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long wprioNo;

    @Column(nullable = false, unique = false)
    private long wprNo;

    @Column(nullable = false, unique = false)
    private String expDt;

    @Column(nullable = false, unique = false)
    private long ioCnt;

    @Column(nullable = false, unique = false)
    private long restCnt;

    @Column(nullable = false, unique = false)
    private String regId;

    @Column(nullable = false, unique = false)
    private String regDt;

    @Column(nullable = false, unique = false)
    private String modId;

    @Column(nullable = false, unique = false)
    private String modDt;
}
