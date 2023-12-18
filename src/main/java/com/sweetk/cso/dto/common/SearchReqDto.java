package com.sweetk.cso.dto.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchReqDto {

    private String startDt;
    private String endDt;
//    private String searchType;
    private String inOut;
    private String outWy;
    private String searchWord;
    private int pageNo = 1;
    private int pageSize = 10;

}
