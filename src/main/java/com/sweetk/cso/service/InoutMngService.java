package com.sweetk.cso.service;

import com.sweetk.cso.dto.*;
import com.sweetk.cso.entity.Stock;
import com.sweetk.cso.repository.AdmRepository;
import com.sweetk.cso.repository.ProductRepository;
import com.sweetk.cso.repository.SalesRepository;
import com.sweetk.cso.repository.StockRepository;
import com.sweetk.cso.repository.custom.SalesCustomRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

//import static org.springframework.security.taglibs.TagLibConfig.logger;

@Log4j2
@Service
@RequiredArgsConstructor
public class InoutMngService {

    final ProductRepository productRepository;
    final StockRepository stockRepository;
    final SalesRepository salesRepository;
    final AdmRepository admRepository;

    //######################## 제품입출고관리
    @Transactional(readOnly = true)
    public Page<StockListRes> getStockList(StockListReq req, Pageable pageable) {
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

        if(admRepository.checkUserPwd(params).equals("0")) {
            return "0";
        }

        return stockRepository.deleteStockByStoNo(params);
    }

    public void excelStockDownload(StockListReq req, HttpServletResponse response) {
        List<StockListRes> list = stockRepository.findStockForExcel(req);
        // UnvrsVO에 UnvrsCollector 세팅
//        for (UnvrsVO unvrsVO : list) {
//            if(unvrsVO.getEstbDate() != null) {
//                unvrsVO.setEstbDateStr(unvrsVO.getEstbDate().toLocalDate().toString());
//            }
//
//            long evalYearSeq = unvrsVO.getEvalYearSeq();
//            String asymbol = unvrsVO.getAsymbol();
//            List<UnvrsCollectorVO> unvrsCollectorVOList = unvrsCollectorMapper.selectListUnvrsCollector(evalYearSeq, asymbol);
//            if (Objects.nonNull(unvrsCollectorVOList)) {
//                unvrsVO.setUnvrsCollectors(String.join(",", unvrsCollectorVOList.stream().map(UnvrsCollectorVO::getCollectorNm).collect(Collectors.toList())));
//            }
//        }

        writeStockExcel(list, "stock", response);
    }

    public void writeStockExcel(List<StockListRes> downloadList, String classNm, HttpServletResponse response) {
        String excelPath = "static/excel/" +  classNm + ".xlsx";
        String fileNm = classNm + "_" + getDatePatterns("yyyyMMddHHmmss");

        log.info(downloadList);

        ClassPathResource resource = new ClassPathResource(excelPath);
        try (InputStream fis = resource.getInputStream()){
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + fileNm + ".xlsx\"");
            OutputStream os = response.getOutputStream();
            Context context = new Context();
            context.putVar("list", downloadList);   //엑셀의 내용을 채우는 아이
            JxlsHelper.getInstance().processTemplate(fis, os, context);
            log.info("### writeExcel success");
        } catch (IOException e) {
            //logger.error(e.getMessage(), e);
            log.info("### writeExcel fail");
        }
    }

    public static String getDatePatterns (String pttn) {
        return new SimpleDateFormat(pttn,new Locale("ko","KR")).format(Calendar.getInstance().getTime());
    }

    //######################## 제품출고이력
    @Transactional(readOnly = true)
    public Page<SalesListRes> getSalesList(SalesListReq req, Pageable pageable) {
        // TODO COM_CM 이름
        return salesRepository.getListBySearchDtoAndPageable(req, pageable);
    }

    public String deleteSales(Map<String, Object> params) {
        log.info("### deleteSales");
        log.info(params);

        if(admRepository.checkUserPwd(params).equals("0")) {
            return "0";
        }

        return salesRepository.deleteSalesBySalesNo(params);
    }

    public void excelSalesDownload(SalesListReq req, HttpServletResponse response) {
        List<SalesListRes> list = salesRepository.findSalesForExcel(req);
        writeSalesExcel(list, "sales", response);
    }

    public void writeSalesExcel(List<SalesListRes> downloadList, String classNm, HttpServletResponse response) {
        String excelPath = "static/excel/" +  classNm + ".xlsx";
        String fileNm = classNm + "_" + getDatePatterns("yyyyMMddHHmmss");

        log.info(downloadList);

        ClassPathResource resource = new ClassPathResource(excelPath);
        try (InputStream fis = resource.getInputStream()){
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + fileNm + ".xlsx\"");
            OutputStream os = response.getOutputStream();
            Context context = new Context();
            context.putVar("list", downloadList);   //엑셀의 내용을 채우는 아이
            JxlsHelper.getInstance().processTemplate(fis, os, context);
            log.info("### writeExcel success");
        } catch (IOException e) {
            //logger.error(e.getMessage(), e);
            log.info("### writeExcel fail");
        }
    }

    //######################## 통계
    @Transactional(readOnly = true)
    public Page<SalesListRes> getStatsaleList(StatsaleListReq req, Pageable pageable) {
        // TODO COM_CM 이름
        return salesRepository.getStatsaleListBySearchDtoAndPageable(req, pageable);
    }
}
