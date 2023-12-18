package com.sweetk.cso.service;

import com.sweetk.cso.entity.Adm;
import com.sweetk.cso.entity.Consumer;
import com.sweetk.cso.entity.Product;
import com.sweetk.cso.repository.AdmRepository;
import com.sweetk.cso.repository.ConsumerRepository;
import com.sweetk.cso.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class InfoMngService {

    final ProductRepository productRepository;
    final AdmRepository admRepository;
    final ConsumerRepository consumerRepository;
    final BCryptPasswordEncoder bCryptPasswordEncoder;

    //############################### 제품 정보 관리 #################################
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
        return productRepository.deleteProductByProCd(String.valueOf(params.get("pro_cd")));
    }

    //############################### 직원 정보 관리 #################################
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
        return admRepository.deleteStaffByAdmId(String.valueOf(params.get("adm_id")));
    }

    //############################### 고객 정보 관리 #################################
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
        return consumerRepository.deleteConsumerByCsmCd(String.valueOf(params.get("csm_cd")));
    }
}
