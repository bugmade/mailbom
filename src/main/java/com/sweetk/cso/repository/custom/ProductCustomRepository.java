package com.sweetk.cso.repository.custom;

import com.sweetk.cso.dto.ProductListReq;
import com.sweetk.cso.dto.ProductListRes;
import com.sweetk.cso.dto.StaffListReq;
import com.sweetk.cso.dto.StaffListRes;
import com.sweetk.cso.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ProductCustomRepository {
    Page<ProductListRes> getListBySearchDtoAndPageable(ProductListReq req, Pageable pageable);

    List<Product> findAll();

    Page<Product> findPageAllByProNm(String proNm, Pageable pageable);

    Product findProductByProCd(String proCd);

    String createProduct(Map<String, Object> params);

    String updateProduct(Map<String, Object> params);

    String deleteProductByProCd(String procd);
}
