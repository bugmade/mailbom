package com.sweetk.cso.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesListRes {
    private long salesNo;
    private long stoNo;
    private String lotNo;
    private String expDt;
    private String proNm;
    private String fromStorage;
    private long outCnt;
    private String outWy;
    private String csmNm;
    private String memo;
    private String regId;
    private String regDt;
    private String modId;
    private String modDt;
}
