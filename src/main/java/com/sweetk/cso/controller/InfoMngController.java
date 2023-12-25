package com.sweetk.cso.controller;

import com.querydsl.jpa.impl.JPADeleteClause;
import com.sweetk.cso.dto.*;
import com.sweetk.cso.dto.pharmComp.PharmCompListReq;
import com.sweetk.cso.entity.Adm;
import com.sweetk.cso.entity.Consumer;
import com.sweetk.cso.entity.Product;
import com.sweetk.cso.service.InfoMngService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Log4j2
@Controller
@RequestMapping("/infoMng")
@RequiredArgsConstructor
public class InfoMngController {

    private final InfoMngService infoMngService;

    //############################### 제품 정보 관리 #################################
    @GetMapping("/product")
    public String list(PharmCompListReq req, Model model) {
        //Page<PharmCompListRes> result = pharmCompService.getList(req, PageRequest.of(req.getPageNo()-1, req.getPageSize()));
        //model.addAttribute("result", result);
        return "/web/infoMng/product";
    }

    @GetMapping("/product1")
    public String product1(ProductListReq req, Model model) {
        Page<ProductListRes> result = infoMngService.getProductList(req, PageRequest.of(req.getPageNo()-1, req.getPageSize()));
        model.addAttribute("result", result);
        return "/web/infoMng/product";
    }

    // 재품 리스트 반환
    @GetMapping("/api/readProductList")
    @ResponseBody
    public List<Product> readProductList(@RequestParam Map<String, Object> params, HttpServletRequest request){
        log.info("### readProductList");
        log.info(params);
        log.info(request);
        return infoMngService.readProductList();
    }

    // 재품 상세정보 반환
    @GetMapping("/api/readProductDetail")
    @ResponseBody
    public Product readProductDetail(@RequestParam Map<String, Object> params, HttpServletRequest request){
        log.info("### readProductDetail");
        log.info(params);
        log.info(request);
        return infoMngService.readProductDetail(params);
    }

    // 재품 등록
    @RequestMapping("/api/createProduct")
    @ResponseBody
    public String createProduct(@RequestParam Map<String, Object> params, HttpServletRequest request){
        log.info("### createProduct");

        return infoMngService.createProduct(params);
    }

    // 재품 수정
    @RequestMapping("/api/updateProduct")
    @ResponseBody
    public String updateProduct(@RequestParam Map<String, Object> params, HttpServletRequest request){
        log.info("### updateProduct");

        return infoMngService.updateProduct(params);
    }

    // 재품 삭제
    @RequestMapping("/api/deleteProduct")
    @ResponseBody
    public String  deleteProduct(@RequestParam Map<String, Object> params, HttpServletRequest request){
        log.info("### deleteProduct");
        log.info(params);

        return infoMngService.deleteProduct(params);
    }

    //############################### 직원 정보 관리 #################################
//    @GetMapping("/staff")
//    public String staff(PharmCompListReq req, Model model)
//    {
//        return "/web/infoMng/staff";
//    }

    @GetMapping("/staff")
    public String staff(StaffListReq req, Model model) {
        Page<StaffListRes> result = infoMngService.getStaffList(req, PageRequest.of(req.getPageNo()-1, req.getPageSize()));
        model.addAttribute("result", result);
        return "/web/infoMng/staff";
    }

    // 직원 리스트 반환
    @GetMapping("/api/readStaffList")
    @ResponseBody
    public List<Adm> readStaffList(@RequestParam Map<String, Object> params, HttpServletRequest request){
        log.info("### readStaffList");
        log.info(params);
        log.info(request);
        return infoMngService.readStaffList();
    }

    // 직원 상세정보 반환
    @GetMapping("/api/readStaffDetail")
    @ResponseBody
    public Adm readStaffDetail(@RequestParam Map<String, Object> params, HttpServletRequest request){
        log.info("### readStaffDetail");
        log.info(params);
        log.info(request);
        return infoMngService.readStaffDetail(params);
    }

    // 직원 등록
    @RequestMapping("/api/createStaff")
    @ResponseBody
    public String createStaff(@RequestParam Map<String, Object> params, HttpServletRequest request){
        log.info("### createStaff");

        return infoMngService.createStaff(params);
    }

    // 직원 수정
    @RequestMapping("/api/updateStaff")
    @ResponseBody
    public String updateStaff(@RequestParam Map<String, Object> params, HttpServletRequest request){
        log.info("### updateStaff");

        return infoMngService.updateStaff(params);
    }

    // 직원 삭제
    @RequestMapping("/api/deleteStaff")
    @ResponseBody
    public String  deleteStaff(@RequestParam Map<String, Object> params, HttpServletRequest request){
        log.info("### deleteStaff");
        log.info(params);

        return infoMngService.deleteStaff(params);
    }

    //############################### 고객 정보 관리 #################################
    @GetMapping("/consumer")
    public String consumer(PharmCompListReq req, Model model)
    {
        return "/web/infoMng/consumer";
    }

    // 고객 리스트 반환
    @GetMapping("/api/readConsumerList")
    @ResponseBody
    public List<Consumer> readConsumerList(@RequestParam Map<String, Object> params, HttpServletRequest request){
        log.info("### readConsumerList");
        log.info(params);
        log.info(request);
        return infoMngService.readConsumerList();
    }

    // 직원 상세정보 반환
    @GetMapping("/api/readConsumerDetail")
    @ResponseBody
    public Consumer readConsumerDetail(@RequestParam Map<String, Object> params, HttpServletRequest request){
        log.info("### readConsumerDetail");
        log.info(params);
        log.info(request);
        return infoMngService.readConsumerDetail(params);
    }

    // 고객 등록
    @RequestMapping("/api/createConsumer")
    @ResponseBody
    public String createConsumer(@RequestParam Map<String, Object> params, HttpServletRequest request){
        log.info("### createConsumer");

        return infoMngService.createConsumer(params);
    }

    // 고객 수정
    @RequestMapping("/api/updateConsumer")
    @ResponseBody
    public String updateConsumer(@RequestParam Map<String, Object> params, HttpServletRequest request){
        log.info("### updateConsumer");

        return infoMngService.updateConsumer(params);
    }

    // 고객 삭제
    @RequestMapping("/api/deleteConsumer")
    @ResponseBody
    public String  deleteConsumer(@RequestParam Map<String, Object> params, HttpServletRequest request){
        log.info("### deleteConsumer");
        log.info(params);

        return infoMngService.deleteConsumer(params);
    }
}
