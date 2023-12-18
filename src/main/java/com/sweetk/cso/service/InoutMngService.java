package com.sweetk.cso.service;

import com.sweetk.cso.dto.StockListReq;
import com.sweetk.cso.dto.StockListRes;
import com.sweetk.cso.dto.pharmComp.PharmCompListReq;
import com.sweetk.cso.dto.pharmComp.PharmCompListRes;
import com.sweetk.cso.entity.Stock;
import com.sweetk.cso.repository.AdmRepository;
import com.sweetk.cso.repository.ProductRepository;
import com.sweetk.cso.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class InoutMngService {

    final ProductRepository productRepository;
    final StockRepository stockRepository;
    final AdmRepository admRepository;

    @Transactional(readOnly = true)
    public Page<StockListRes> getList(StockListReq req, Pageable pageable) {
        // TODO COM_CM 이름
        return stockRepository.getListBySearchDtoAndPageable(req, pageable);
    }

    public List<StockListRes> readStockList(Map<String, Object> params){
        log.info("### readStockList");
        return stockRepository.findAllStock();
    }

    public Stock readStockDetail(Map<String, Object> params){
        log.info("### readStockDetail");
        log.info(params);
        return stockRepository.findStockByProCd(String.valueOf(params.get("pro_cd")));
    }

    public String createStock(Map<String, Object> params) {
        log.info("### createStock");
        return stockRepository.createStock(params);
    }

    public String deleteStock(Map<String, Object> params) {
        log.info("### deleteStock");
        log.info(params);
        return stockRepository.deleteStockByStoNo(String.valueOf(params.get("sto_no")));
    }
}
