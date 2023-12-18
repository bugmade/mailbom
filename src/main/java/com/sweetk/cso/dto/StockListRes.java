package com.sweetk.cso.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockListRes {
    private long stoNo;
    private String proCd;
    private String proNm;
    private String inOut;
    private long ioCnt;
    private String outWy;
    private String csmNm;
    private String memo;
    private String RegId;
    private String RegDt;
    private String ModId;
    private String ModDt;
}
