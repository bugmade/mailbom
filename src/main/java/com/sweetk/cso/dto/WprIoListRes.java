package com.sweetk.cso.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WprIoListRes {
    private long wprioNo;
    private long wprNo;
    private String wprNm;
    private String expDt;
    private long ioCnt;
    private long restCnt;
    private String regId;
    private String regDt;
    private String modId;
    private String modDt;
}
