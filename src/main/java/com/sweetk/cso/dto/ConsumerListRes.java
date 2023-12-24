package com.sweetk.cso.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsumerListRes {
    private long csmNo;
    private String csmCd;
    private String csmNm;
    private String bizNo;
    private String addr;
    private String picNm;
    private String telNo;
    private String email;
    private String csmDt;
    private String regId;
    private String regDt;
    private String modId;
    private String modDt;
}
