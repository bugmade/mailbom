package com.sweetk.cso.repository.custom.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sweetk.cso.dto.StaffListReq;
import com.sweetk.cso.dto.StaffListRes;
import com.sweetk.cso.dto.StockListReq;
import com.sweetk.cso.dto.StockListRes;
import com.sweetk.cso.dto.common.SearchReqDto;
import com.sweetk.cso.entity.Adm;
import com.sweetk.cso.entity.Product;
import com.sweetk.cso.entity.Stock;
import com.sweetk.cso.repository.custom.AdmCustomRepository;
import com.sweetk.cso.repository.custom.StockCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.sweetk.cso.entity.QConsumer.consumer;
import static com.sweetk.cso.entity.QProduct.product;
import static com.sweetk.cso.entity.QStock.stock;
import static com.sweetk.cso.entity.QAdm.adm;

@Log4j2
@Repository
@RequiredArgsConstructor
public class AdmCustomRepositoryImpl implements AdmCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager entityManager;

    @Override
    public Page<StaffListRes> getListBySearchDtoAndPageable(StaffListReq req, Pageable pageable) {
        log.info("### getListBySearchDtoAndPageable");

        List<StaffListRes> list = jpaQueryFactory
                .select(Projections.fields(StaffListRes.class,
                        adm.admNo,
                        adm.admId,
                        adm.admPw,
                        adm.admNm,
                        adm.email,
                        adm.telNo,
                        adm.regId,
                        adm.regDt
                )).from(adm)
//                .leftJoin(product)
//                .on(stock.proCd.eq(product.proCd))
//                .leftJoin(consumer)
//                .on(stock.csmCd.eq(consumer.csmCd))
                .where(searchByTextInput(req))
                .orderBy(adm.admNo.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = jpaQueryFactory.select(adm.count())
                .from(adm)
//                .leftJoin(product)
//                .on(stock.proCd.eq(product.proCd))
//                .leftJoin(consumer)
//                .on(stock.csmCd.eq(consumer.csmCd))
                .where(searchByTextInput(req))
                .fetchOne();

        return new PageImpl<>(list, pageable, totalCount != null ? totalCount : 0L);
    }

    private BooleanExpression searchByTextInput(SearchReqDto req){
        BooleanExpression searchExpression = null;
        String searchWord = req.getSearchWord();

        if(!searchWord.equals("")) {
            searchExpression = adm.admNm.contains(searchWord);
        }

//        String inOut = req.getInOut();
//        String outWy = req.getOutWy();
//        String searchWord = req.getSearchWord();
//
//        if (!StringUtils.hasText(inOut))    inOut = "ALL";
//        if (!StringUtils.hasText(outWy))    outWy = "ALL";
//        if (!StringUtils.hasText(searchWord))    searchWord = "";
//
//        log.info("### searchByTextInput [inOut:" + inOut + ", outWy:" + outWy + ", searchWord:" + searchWord + "]");

        // 입고일때
//        if (inOut.equals("IN")) {
//            searchExpression = stock.inOut.eq(inOut);
//        }
//        else if (inOut.equals("OUT")) {    // 출고일때
//            if (!outWy.equals("ALL")) {
//                if(outWy.equals("BTOB") && !searchWord.equals(""))
//                    searchExpression = stock.inOut.eq(inOut).and(stock.outWy.eq(outWy)).and(consumer.csmNm.contains(searchWord));
//                else
//                    searchExpression = stock.inOut.eq(inOut).and(stock.outWy.eq(outWy));
//            } else {
//                searchExpression = stock.inOut.eq(inOut);
//            }
//        }
        return searchExpression;
    }

    @Override
    public List<Adm> findAllStaff() {
        log.info("### findAll");

        // @@@ 요거는 다시 봐야함
//        return entityManager
//                .createNativeQuery("SELECT a.STO_NO, a.PRO_CD, b.PRO_NM AS PRO_NM, a.IN_OUT, a.IO_CNT, a.OUT_WY, a.CSM_CD, a.MEMO, a.REG_ID, a.REG_DT, a.MOD_ID, a.MOD_DT\n" +
//                        "FROM stock a\n" +
//                        "LEFT JOIN product b\n" +
//                        "ON a.PRO_CD = b.PRO_CD")
//                //.setParameter(1, "111")
//                .getResultList();

        return jpaQueryFactory
                .selectFrom(adm)
                //.where(product.csoNm.contains(csoNm))
                //.orderBy(product.csoCd.asc())
                .fetch();
    }

    @Override
    public Adm findStaffByAdmId(String admId) {
        log.info("### findStaffByAdmId");
        log.info(admId);
        return jpaQueryFactory
                .selectFrom(adm)
                .where(adm.admId.eq(admId))
                .fetchOne();
    }

    @Transactional
    @Override
    public String createStaff(Map<String, Object> params, String pwd) {
        Date now = new Date();

        log.info("### createStaff");
        log.info(params);

        //ADM_ID가 이미 존재하는지 체크
        Adm adm = findStaffByAdmId(String.valueOf(params.get("adm_id")));
        if(adm != null) {
            log.info("### already exist");
            return "0";
        }

        return String.valueOf(entityManager
                .createNativeQuery("INSERT INTO adm (ADM_ID, ADM_PW, ADM_NM, EMAIL, TEL_NO, REG_ID, REG_DT) VALUES (?,?,?,?,?,?,?)")
                .setParameter(1, String.valueOf(params.get("adm_id")))
                .setParameter(2, pwd)
                .setParameter(3, String.valueOf(params.get("adm_nm")))
                .setParameter(4, String.valueOf(params.get("email")))
                .setParameter(5, String.valueOf(params.get("tel_no")))
                .setParameter(6, String.valueOf(params.get("login_id")))
                .setParameter(7, now)
                .executeUpdate());
    }

    @Transactional
    @Override
    public String updateStaff(Map<String, Object> params) {
        Date now = new Date();

        log.info("### updateStaff");
        log.info(params);

        return String.valueOf(entityManager
                .createNativeQuery("UPDATE adm SET ADM_NM = ?, EMAIL = ?, TEL_NO = ?, MOD_ID = ?, MOD_DT = ? WHERE ADM_ID = ?")
                .setParameter(1, String.valueOf(params.get("adm_nm")))
                .setParameter(2, String.valueOf(params.get("email")))
                .setParameter(3, String.valueOf(params.get("tel_no")))
                .setParameter(4, String.valueOf(params.get("login_id")))
                .setParameter(5, now)
                .setParameter(6, String.valueOf(params.get("adm_id")))
                .executeUpdate());
    }

    @Transactional
    @Override
    public String deleteStaffByAdmId(String admId) {
        log.info("### deleteStaffByAdmId");
        log.info(admId);

        return String.valueOf(jpaQueryFactory
                .delete(adm)
                .where(adm.admId.eq(admId))
                .execute());
    }

//    @Override
//    public Page<Stock> findPageAllByProNm(String proNm, Pageable pageable) {
//        Long totCnt = jpaQueryFactory
//                .select(stock.count())
//                .from(stock)
//                .where(stock.proCd.contains(proNm))
//                .fetchOne();
//
//        List<Stock> stockList = jpaQueryFactory
//                .select(stock)
//                .from(stock)
//                .where(stock.proCd.contains(proNm))
//                .orderBy(stock.proCd.asc())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        return new PageImpl<>(stockList, pageable, totCnt != null ? totCnt : 0L);
//    }

}
