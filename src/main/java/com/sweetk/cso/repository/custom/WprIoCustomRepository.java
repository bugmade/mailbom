package com.sweetk.cso.repository.custom;

import com.sweetk.cso.dto.WprIoListReq;
import com.sweetk.cso.dto.WprIoListRes;
import com.sweetk.cso.dto.WrapperListReq;
import com.sweetk.cso.dto.WrapperListRes;
import com.sweetk.cso.entity.WprIo;
import com.sweetk.cso.entity.Wrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface WprIoCustomRepository {
    Page<WprIoListRes> getListBySearchDtoAndPageable(WprIoListReq req, Pageable pageable);

    List<WprIo> findAll();

//    Page<Wrapper> findPageAllByProNm(String proNm, Pageable pageable);
//
//    WprIo readWrapperDetail(Map<String, Object> params);
//
//    String createWrapper(Map<String, Object> params);
//
//    String updateWrapper(Map<String, Object> params);
//
//    String deleteWrapper(Map<String, Object> params);
}
