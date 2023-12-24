package com.sweetk.cso.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class StaffListRes {
    private long admNo;
    private String admId;
    private String admPw;
    private String admNm;
    private String email;
    private String telNo;
    private String regId;
    private LocalDateTime regDt;
}
