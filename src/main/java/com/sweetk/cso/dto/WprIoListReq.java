package com.sweetk.cso.dto;

import com.sweetk.cso.dto.common.SearchReqDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WprIoListReq extends SearchReqDto {
    private String wprNo;
}
