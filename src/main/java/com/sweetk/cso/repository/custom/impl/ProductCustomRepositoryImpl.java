package com.sweetk.cso.repository.custom.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sweetk.cso.dto.ProductListReq;
import com.sweetk.cso.dto.ProductListRes;
import com.sweetk.cso.dto.StaffListReq;
import com.sweetk.cso.dto.StaffListRes;
import com.sweetk.cso.dto.common.SearchReqDto;
import com.sweetk.cso.entity.Product;
import com.sweetk.cso.repository.custom.ProductCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.sweetk.cso.entity.QAdm.adm;
import static com.sweetk.cso.entity.QProduct.product;

@Log4j2
@Repository
@RequiredArgsConstructor
public class ProductCustomRepositoryImpl implements ProductCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager entityManager;
    final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Page<ProductListRes> getListBySearchDtoAndPageable(ProductListReq req, Pageable pageable) {
        log.info("### getListBySearchDtoAndPageable");

        List<ProductListRes> list = jpaQueryFactory
                .select(Projections.fields(ProductListRes.class,
                        product.proNo,
                        product.proCd,
                        product.proNm,
                        product.proDt,
                        product.hqStorage,
                        product.firstStorage,
                        product.regId,
                        product.regDt
                )).from(product)
                .where(searchByTextInput(req))
                .orderBy(product.proNo.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = jpaQueryFactory.select(product.count())
                .from(product)
                .where(searchByTextInput(req))
                .fetchOne();

        return new PageImpl<>(list, pageable, totalCount != null ? totalCount : 0L);
    }

    private BooleanExpression searchByTextInput(SearchReqDto req){
        BooleanExpression searchExpression = null;
        String searchWord = req.getSearchWord();

        if(StringUtils.hasText(searchWord)) {
            searchExpression = product.proNm.contains(searchWord);
        }
        return searchExpression;
    }

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
        Long hqStorage = Long.parseLong(String.valueOf(params.get("hq_storage")));
        Long firstStorage = Long.parseLong(String.valueOf(params.get("first_storage")));

        // PRO_CD가 이미 존재하는지 체크
        Product product = findProductByProCd(String.valueOf(params.get("pro_cd")));
        if(product != null) {
            log.info("### already exist");
            return "0";
        }

        return String.valueOf(entityManager
                .createNativeQuery("INSERT INTO product (PRO_CD, PRO_NM, PRO_DT, HQ_STORAGE, FIRST_STORAGE, REG_ID, REG_DT) VALUES (?,?,?,?,?,?,?)")
                .setParameter(1, String.valueOf(params.get("pro_cd")))
                .setParameter(2, String.valueOf(params.get("pro_nm")))
                .setParameter(3, String.valueOf(params.get("pro_dt")))
                .setParameter(4, hqStorage)
                .setParameter(5, firstStorage)
                .setParameter(6, String.valueOf(params.get("login_id")))
                .setParameter(7, now)
                .executeUpdate());
    }

    @Transactional
    @Override
    public String updateProduct(Map<String, Object> params) {
        Date now = new Date();

        log.info("### updateProduct");
        log.info(params);
        Long hqStorage = Long.parseLong(String.valueOf(params.get("hq_storage")));
        Long firstStorage = Long.parseLong(String.valueOf(params.get("first_storage")));

        return String.valueOf(entityManager
                .createNativeQuery("UPDATE product SET PRO_NM = ?, HQ_STORAGE = ?, FIRST_STORAGE = ?, PRO_DT = ?, MOD_ID = ?, MOD_DT = ? WHERE PRO_CD = ?")
                .setParameter(1, String.valueOf(params.get("pro_nm")))
                .setParameter(2, hqStorage)
                .setParameter(3, firstStorage)
                .setParameter(4, String.valueOf(params.get("pro_dt")))
                .setParameter(5, String.valueOf(params.get("login_id")))
                .setParameter(6, now)
                .setParameter(7, String.valueOf(params.get("pro_cd")))
                .executeUpdate());
    }

    @Transactional
    @Override
    public String deleteProductByProCd(Map<String, Object> params) {
        log.info("### deleteProductByProCd");

        // user pwd 체크
        if(checkUserPwd(params).equals("0")) {
            return "0";
        }

        String proCd = String.valueOf(params.get("pro_cd"));
        return String.valueOf(jpaQueryFactory
                .delete(product)
                .where(product.proCd.eq(proCd))
                .execute());
    }

    // 비밀번호 일치여부 체크, @@@ 중복
    public String checkUserPwd(Map<String, Object> params) {
        log.info("### checkUserPwd");

        String db_pwd = jpaQueryFactory
                .select(adm.admPw)
                .from(adm)
                .where(adm.admId.eq(String.valueOf(params.get("login_id"))))
                .fetchOne();

        if(bCryptPasswordEncoder.matches(String.valueOf(params.get("pwd")), db_pwd)) {
            return "1";
        } else {
            log.info("### invalid pwd");
            return "0";
        }
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
