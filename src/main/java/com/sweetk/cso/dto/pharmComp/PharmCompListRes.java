package com.sweetk.cso.dto.pharmComp;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PharmCompListRes {

    private Long pharmCompNo;
    private String pharmCompCd;
    private String pharmCompNm;
    private String bizRegNo;
    private String repNm;
    private String taxTypeCcd;
    private String taxTypeCcdNm;
    private BigDecimal leastAmt;
    private String zipNo;
    private String addr;
    private String addrDtl;
    private String modId;
    private String modDt;

}
