package com.sweetk.cso.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class Adm {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long admNo;

    @Column(name = "ADM_ID", nullable = false)
    private String admId;

    @Column(name = "ADM_PW",nullable = false)
    private String admPw;

    @Column(name = "ADM_NM",nullable = false)
    private String admNm;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "TEL_NO")
    private String telNo;

    @Column(name = "REG_ID")
    private String regId;

    @Column(name = "REG_DT")
    private LocalDateTime regDt;

}
