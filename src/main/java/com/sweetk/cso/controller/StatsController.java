package com.sweetk.cso.controller;

import com.sweetk.cso.dto.SalesListReq;
import com.sweetk.cso.dto.SalesListRes;
import com.sweetk.cso.dto.StockListReq;
import com.sweetk.cso.dto.StockListRes;
import com.sweetk.cso.entity.Consumer;
import com.sweetk.cso.entity.Product;
import com.sweetk.cso.entity.Stock;
import com.sweetk.cso.service.InfoMngService;
import com.sweetk.cso.service.InoutMngService;
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
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatsController {

    private final InoutMngService inoutMngService;
    private final InfoMngService infoMngService;

    @GetMapping("/statsale")
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

        return "/web/stats/statsale";
    }
}
