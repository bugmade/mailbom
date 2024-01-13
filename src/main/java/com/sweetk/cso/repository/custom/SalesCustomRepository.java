package com.sweetk.cso.repository.custom;

import com.sweetk.cso.dto.*;
import com.sweetk.cso.entity.Sales;
import com.sweetk.cso.entity.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface SalesCustomRepository {

    Page<SalesListRes> getListBySearchDtoAndPageable(SalesListReq req, Pageable pageable);
//    List<SalesListRes> findAllStock();
    List<SalesListRes> findSalesForExcel(SalesListReq req);
//
//    Page<Sales> findPageAllByProNm(String proNm, Pageable pageable);
//
//    Sales findStockByProCd(String proCd);
//
//    String createStock(Map<String, Object> params);
//
    String deleteSalesBySalesNo(Map<String, Object> params);

    Page<SalesListRes> getStatsaleListBySearchDtoAndPageable(StatsaleListReq req, Pageable pageable);
}