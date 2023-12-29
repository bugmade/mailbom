package com.sweetk.cso.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockListRes {
    private long stoNo;
    private String proCd;
    private String lotNo;
    private String expDt;
    private String proNm;
    private String inOut;
    private long ioCnt;
    private long restCnt;
    private String fromStorage;
    private String memo;
    private String regId;
    private String regDt;
    private String modId;
    private String modDt;
}
