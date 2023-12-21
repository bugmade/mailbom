package com.sweetk.cso.repository.custom.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sweetk.cso.entity.Product;
import com.sweetk.cso.repository.custom.ProductCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.sweetk.cso.entity.QProduct.product;

@Log4j2
@Repository
@RequiredArgsConstructor
public class ProductCustomRepositoryImpl implements ProductCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager entityManager;

    @Override
    public List<Product> findAll() {
        return jpaQueryFactory
                .selectFrom(product)
                //.where(product.csoNm.contains(csoNm))
                //.orderBy(product.csoCd.asc())
                .fetch();
    }

    @Override
    public Product findProductByProCd(String proCd) {
        log.info("### findProductByProCd");
        log.info(proCd);
        return jpaQueryFactory
                .selectFrom(product)
                .where(product.proCd.eq(proCd))
                .fetchOne();
    }

    @Transactional
    @Override
    public String createProduct(Map<String, Object> params) {
        Date now = new Date();

        log.info("### createProduct");
        log.info(params);
        Long proSt = Long.parseLong(String.valueOf(params.get("pro_st")));
        Long outFirstSt = Long.parseLong(String.valueOf(params.get("out_first_st")));

        // PRO_CD가 이미 존재하는지 체크
        Product product = findProductByProCd(String.valueOf(params.get("pro_cd")));
        if(product != null) {
            log.info("### already exist");
            return "0";
        }

        return String.valueOf(entityManager
                .createNativeQuery("INSERT INTO product (PRO_CD, PRO_NM, PRO_DT, PRO_ST, OUT_FIRST_ST, REG_DT) VALUES (?,?,?,?,?,?)")
                .setParameter(1, String.valueOf(params.get("pro_cd")))
                .setParameter(2, String.valueOf(params.get("pro_nm")))
                .setParameter(3, String.valueOf(params.get("pro_dt")))
                .setParameter(4, proSt)
                .setParameter(5, outFirstSt)
                .setParameter(6, now)
                .executeUpdate());
    }

    @Transactional
    @Override
    public String updateProduct(Map<String, Object> params) {
        Date now = new Date();

        log.info("### updateProduct");
        log.info(params);
        Long proSt = Long.parseLong(String.valueOf(params.get("pro_st")));
        Long outFirstSt = Long.parseLong(String.valueOf(params.get("out_first_st")));

        return String.valueOf(entityManager
                .createNativeQuery("UPDATE product SET PRO_NM = ?, PRO_ST = ?, OUT_FIRST_ST = ?, PRO_DT = ?, MOD_DT = ? WHERE PRO_CD = ?")
                .setParameter(1, String.valueOf(params.get("pro_nm")))
                .setParameter(2, proSt)
                .setParameter(3, outFirstSt)
                .setParameter(4, String.valueOf(params.get("pro_dt")))
                .setParameter(5, now)
                .setParameter(6, String.valueOf(params.get("pro_cd")))
                .executeUpdate());
    }

    @Transactional
    @Override
    public String deleteProductByProCd(String proCd) {
        log.info("### deleteProductByProCd");
        log.info(proCd);
        return String.valueOf(jpaQueryFactory
                .delete(product)
                .where(product.proCd.eq(proCd))
                .execute());
    }
    @Override
    public Page<Product> findPageAllByProNm(String proNm, Pageable pageable) {
        Long totCnt = jpaQueryFactory
                .select(product.count())
                .from(product)
                .where(product.proNm.contains(proNm))
                .fetchOne();

        List<Product> productList = jpaQueryFactory
                .select(product)
                .from(product)
                .where(product.proNm.contains(proNm))
                .orderBy(product.proCd.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(productList, pageable, totCnt != null ? totCnt : 0L);
    }

}
