package com.sweetk.cso.repository.custom;

import com.sweetk.cso.dto.StaffListReq;
import com.sweetk.cso.dto.StaffListRes;
import com.sweetk.cso.dto.StockListReq;
import com.sweetk.cso.dto.StockListRes;
import com.sweetk.cso.entity.Adm;
import com.sweetk.cso.entity.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface AdmCustomRepository {

    Page<StaffListRes> getListBySearchDtoAndPageable(StaffListReq req, Pageable pageable);

    List<Adm> findAllStaff();

//    Page<Stock> findPageAllByProNm(String proNm, Pageable pageable);
//
    Adm findStaffByAdmId(String admId);
//
    String createStaff(Map<String, Object> params, String pwd);

    String updateStaff(Map<String, Object> params);

    String deleteStaffByAdmId(Map<String, Object> params);

    String checkUserPwd(Map<String, Object> params);
}
