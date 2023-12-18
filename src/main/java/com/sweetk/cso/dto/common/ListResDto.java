package com.sweetk.cso.dto.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListResDto<T> {

    private long totalCount;
    private List<T> list;

    @Builder
    public ListResDto(long totalCount, List<T> list) {
        this.totalCount = totalCount;
        this.list = list;
    }

}
