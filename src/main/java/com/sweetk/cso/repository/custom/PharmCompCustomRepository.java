package com.sweetk.cso.repository.custom;

import com.sweetk.cso.dto.pharmComp.PharmCompListReq;
import com.sweetk.cso.dto.pharmComp.PharmCompListRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PharmCompCustomRepository {
    Page<PharmCompListRes> getListBySearchDtoAndPageable(PharmCompListReq req, Pageable pageable);

}
