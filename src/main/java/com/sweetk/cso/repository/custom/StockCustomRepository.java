package com.sweetk.cso.repository.custom;

import com.sweetk.cso.dto.StockListReq;
import com.sweetk.cso.dto.StockListRes;
import com.sweetk.cso.dto.pharmComp.PharmCompListReq;
import com.sweetk.cso.dto.pharmComp.PharmCompListRes;
import com.sweetk.cso.entity.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface StockCustomRepository {

    Page<StockListRes> getListBySearchDtoAndPageable(StockListReq req, Pageable pageable);
    List<StockListRes> findAllStock();
    List<StockListRes> findStockForExcel(StockListReq req);

    Page<Stock> findPageAllByProNm(String proNm, Pageable pageable);

    Stock findStockByProCd(String proCd);

    String createStock(Map<String, Object> params);

    String deleteStockByStoNo(String stoNo);
}
