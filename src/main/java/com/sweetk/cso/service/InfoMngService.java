package com.sweetk.cso.service;

import com.sweetk.cso.dto.*;
import com.sweetk.cso.entity.Adm;
import com.sweetk.cso.entity.Consumer;
import com.sweetk.cso.entity.Product;
import com.sweetk.cso.entity.Wrapper;
import com.sweetk.cso.repository.AdmRepository;
import com.sweetk.cso.repository.ConsumerRepository;
import com.sweetk.cso.repository.ProductRepository;
import com.sweetk.cso.repository.WrapperRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class InfoMngService {

    final ProductRepository productRepository;
    final WrapperRepository wrapperRepository;
    final AdmRepository admRepository;
    final ConsumerRepository consumerRepository;
    final BCryptPasswordEncoder bCryptPasswordEncoder;

    //############################### 포장지 정보 관리 #################################
    @Transactional(readOnly = true)
    public Page<WrapperListRes> getWrapperList(WrapperListReq req, Pageable pageable) {
        return wrapperRepository.getListBySearchDtoAndPageable(req, pageable);
    }

    public String createWrapper(Map<String, Object> params) {
        log.info("### createWrapper");
        return wrapperRepository.createWrapper(params);
    }

    public String deleteWrapper(Map<String, Object> params) {
        log.info("### deleteWrapper");
        log.info(params);

        if(admRepository.checkUserPwd(params).equals("0")) {
            return "0";
        }

        return wrapperRepository.deleteWrapper(params);
    }

    public Wrapper readWrapperDetail(Map<String, Object> params){
        log.info("### readWrapperDetail");
        log.info(params);
        return wrapperRepository.readWrapperDetail(params);
    }

    public String updateWrapper(Map<String, Object> params) {
        log.info("### updateWrapper");
        return wrapperRepository.updateWrapper(params);
    }

    public List<Wrapper> readWrapperList() {
        log.info("### readWrapperList");
        return wrapperRepository.readWrapperList();
    }

    //############################### 제품 정보 관리 #################################
    @Transactional(readOnly = true)
    public Page<ProductListRes> getProductList(ProductListReq req, Pageable pageable) {
        return productRepository.getListBySearchDtoAndPageable(req, pageable);
    }

    public List<Product> readProductList(){
        log.info("### readProductList");
        return productRepository.findAll();
    }

    public Product readProductDetail(Map<String, Object> params){
        log.info("### readProductDetail");
        log.info(params);
        return productRepository.findProductByProCd(String.valueOf(params.get("pro_cd")));
    }

    public String createProduct(Map<String, Object> params) {
        log.info("### createProduct");
        return productRepository.createProduct(params);
    }

    public String updateProduct(Map<String, Object> params) {
        log.info("### updateProduct");
        return productRepository.updateProduct(params);
    }

    public String deleteProduct(Map<String, Object> params) {
        log.info("### deleteProduct");
        log.info(params);

        if(admRepository.checkUserPwd(params).equals("0")) {
            return "0";
        }

        return productRepository.deleteProductByProCd(params);
    }

    //############################### 직원 정보 관리 #################################
    @Transactional(readOnly = true)
    public Page<StaffListRes> getStaffList(StaffListReq req, Pageable pageable) {
        return admRepository.getListBySearchDtoAndPageable(req, pageable);
    }

    public List<Adm> readStaffList() {
        log.info("### readStaffList");
        return admRepository.findAllStaff();
    }

    public Adm readStaffDetail(Map<String, Object> params){
        log.info("### readStaffDetail");
        log.info(params);
        return admRepository.findStaffByAdmId(String.valueOf(params.get("adm_id")));
    }

    public String createStaff(Map<String, Object> params) {
        String pwd = bCryptPasswordEncoder.encode(String.valueOf(params.get("adm_pw")));

        log.info("### createStaff");
        return admRepository.createStaff(params, pwd);
    }

    public String updateStaff(Map<String, Object> params) {
        log.info("### updateStaff");
        return admRepository.updateStaff(params);
    }

    public String deleteStaff(Map<String, Object> params) {
        log.info("### deleteStaff");
        log.info(params);

        if(admRepository.checkUserPwd(params).equals("0")) {
            return "0";
        }

        return admRepository.deleteStaffByAdmId(params);
    }

    //############################### 고객 정보 관리 #################################
    @Transactional(readOnly = true)
    public Page<ConsumerListRes> getConsumerList(ConsumerListReq req, Pageable pageable) {
        return consumerRepository.getListBySearchDtoAndPageable(req, pageable);
    }
    public List<Consumer> readConsumerList() {
        log.info("### readConsumerList");
        return consumerRepository.findAllConsumer();
    }

    public Consumer readConsumerDetail(Map<String, Object> params){
        log.info("### readConsumerDetail");
        log.info(params);
        return consumerRepository.findConsumerByCsmCd(String.valueOf(params.get("csm_cd")));
    }

    public String createConsumer(Map<String, Object> params) {
        log.info("### createConsumer");
        return consumerRepository.createConsumer(params);
    }

    public String updateConsumer(Map<String, Object> params) {
        log.info("### updateConsumer");
        return consumerRepository.updateConsumer(params);
    }

    public String deleteConsumer(Map<String, Object> params) {
        log.info("### deleteConsumer");
        log.info(params);

        if(admRepository.checkUserPwd(params).equals("0")) {
            return "0";
        }

        return consumerRepository.deleteConsumerByCsmCd(params);
    }
}
