package com.sweetk.cso.dto;

import com.sweetk.cso.dto.common.SearchReqDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockListReq extends SearchReqDto {
    private String proCd;
    private String inOut;
    private String outWy;
    private String datepicker;
}
