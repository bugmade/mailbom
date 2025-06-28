package com.sweetk.cso.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductListRes {
    private long proNo;
    private String proCd;
    private String proNm;
    private String proDt;
    private long hqStorage;
    private long firstStorage;
    private long bongBox;
    private String regId;
    private String regDt;
    private String modId;
    private String modDt;
}
