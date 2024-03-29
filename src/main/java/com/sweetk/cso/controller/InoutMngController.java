package com.sweetk.cso.controller;

import com.sweetk.cso.dto.*;
import com.sweetk.cso.dto.pharmComp.PharmCompListReq;
import com.sweetk.cso.dto.pharmComp.PharmCompListRes;
import com.sweetk.cso.entity.Consumer;
import com.sweetk.cso.entity.Product;
import com.sweetk.cso.entity.Stock;
import com.sweetk.cso.entity.Wrapper;
import com.sweetk.cso.service.InfoMngService;
import com.sweetk.cso.service.InoutMngService;
import com.sweetk.cso.service.PharmCompService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Log4j2
@Controller
@RequestMapping("/inoutMng")
@RequiredArgsConstructor
public class InoutMngController {

    private final InoutMngService inoutMngService;
    private final InfoMngService infoMngService;

    @GetMapping("/stock")
    public String stock(StockListReq req, Model model) {
        Page<StockListRes> result = inoutMngService.getStockList(req, PageRequest.of(req.getPageNo()-1, req.getPageSize()));
        model.addAttribute("result", result);

        log.info("### stock : get select list vvv ");

        List<Product> product = infoMngService.readProductList();
        log.info(product);
        model.addAttribute("product", product);

        List<Consumer> consumer = infoMngService.readConsumerList();
        log.info(consumer);
        model.addAttribute("consumer", consumer);

        log.info("### stock : get select list ^^^ ");

        return "/web/inoutMng/stock";
    }

    // 재품 리스트 반환
    @GetMapping("/api/readStockList")
    @ResponseBody
    public List<StockListRes> readStockList(@RequestParam Map<String, Object> params, HttpServletRequest request){
        log.info("### readStockList");
        log.info(params);
        log.info(request);

        return inoutMngService.readStockList(params);
    }

    // 재품 상세정보 반환
    @GetMapping("/api/readStockDetail")
    @ResponseBody
    public Stock readStockDetail(@RequestParam Map<String, Object> params, HttpServletRequest request){
        log.info("### readStockDetail");
        log.info(params);
        log.info(request);
        return inoutMngService.readStockDetail(params);
    }

    // 재품 등록
    @RequestMapping("/api/createStock")
    @ResponseBody
    public String createStock(@RequestParam Map<String, Object> params, HttpServletRequest request){
        log.info("### createStock");

        return inoutMngService.createStock(params);
    }

    // 입고 삭제
    @RequestMapping("/api/deleteStock")
    @ResponseBody
    public String  deleteStock(@RequestParam Map<String, Object> params, HttpServletRequest request){
        log.info("### deleteStock");
        log.info(params);

        return inoutMngService.deleteStock(params);
    }

    // 엑셀 다운로드
    @RequestMapping("/api/excelStockDownload")
    //@ResponseBody --> @@@ 얘는 있거나 말거나 엑셀 다운로드와 관계없음???
    public void excelStockDownload(StockListReq req, HttpServletResponse response){
        log.info("### excelStockDownload");
        log.info(req);

        inoutMngService.excelStockDownload(req, response);
    }

    // ###############################  제품출고이력
    @GetMapping("/sales")
    public String sales(SalesListReq req, Model model) {
        Page<SalesListRes> result = inoutMngService.getSalesList(req, PageRequest.of(req.getPageNo()-1, req.getPageSize()));
        model.addAttribute("result", result);

        log.info("### sales : get select list vvv ");

        List<Product> product = infoMngService.readProductList();
        log.info(product);
        model.addAttribute("product", product);

        List<Consumer> consumer = infoMngService.readConsumerList();
        log.info(consumer);
        model.addAttribute("consumer", consumer);

        log.info("### sales : get select list ^^^ ");

        return "/web/inoutMng/sales";
    }

    // 출고 삭제
    @RequestMapping("/api/deleteSales")
    @ResponseBody
    public String  deleteSales(@RequestParam Map<String, Object> params, HttpServletRequest request){
        log.info("### deleteSales");
        log.info(params);

        return inoutMngService.deleteSales(params);
    }

    @RequestMapping("/api/excelSalesDownload")
    //@ResponseBody --> @@@ 얘는 있거나 말거나 엑셀 다운로드와 관계없음???
    public void excelSalesDownload(SalesListReq req, HttpServletResponse response){
        log.info("### excelSalesDownload");
        log.info(req);

        inoutMngService.excelSalesDownload(req, response);
    }

    // ###############################  포장지입출고
    @GetMapping("/wpr_io")
    public String wpr_io(WprIoListReq req, Model model) {
        Page<WprIoListRes> result = inoutMngService.getWprIoList(req, PageRequest.of(req.getPageNo()-1, req.getPageSize()));
        model.addAttribute("result", result);

        List<Wrapper> wrapper = infoMngService.readWrapperList();
        log.info(wrapper);
        model.addAttribute("wrapper", wrapper);

        return "/web/inoutMng/wpr_io";
    }

    // 입고/등록
    @RequestMapping("/api/createWprIo")
    @ResponseBody
    public String createWprIo(@RequestParam Map<String, Object> params, HttpServletRequest request){
        log.info("### createWprIo");

        return inoutMngService.createWprIo(params);
    }

    // 출고/삭제
    @RequestMapping("/api/deleteWprIo")
    @ResponseBody
    public String  deleteWprIo(@RequestParam Map<String, Object> params, HttpServletRequest request){
        log.info("### deleteWprIo");
        log.info(params);

        return inoutMngService.deleteWprIo(params);
    }
}
