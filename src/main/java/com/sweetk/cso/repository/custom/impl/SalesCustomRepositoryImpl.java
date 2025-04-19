package com.sweetk.cso.repository.custom.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sweetk.cso.dto.*;
import com.sweetk.cso.entity.Stock;
import com.sweetk.cso.repository.custom.SalesCustomRepository;
import com.sweetk.cso.repository.custom.StockCustomRepository;
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
import static com.sweetk.cso.entity.QSales.sales;
import static com.sweetk.cso.entity.QStock.stock;
import static com.sweetk.cso.entity.QConsumer.consumer;

@Log4j2
@Repository
@RequiredArgsConstructor
public class SalesCustomRepositoryImpl implements SalesCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager entityManager;
    final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Page<SalesListRes> getListBySearchDtoAndPageable(SalesListReq req, Pageable pageable) {
        log.info("### getListBySearchDtoAndPageable");

        List<SalesListRes> list = jpaQueryFactory
                .select(Projections.fields(SalesListRes.class,
                        sales.salesNo,
                        stock.stoNo,
                        stock.lotNo,
                        stock.expDt,
                        product.proNm,
                        stock.fromStorage,
                        sales.outCnt,
                        sales.outWy,
                        consumer.csmNm,
                        sales.memo,
                        sales.regId,
                        sales.regDt,
                        sales.modId,
                        sales.modDt
                )).from(sales)
                .leftJoin(stock)
                .on(stock.stoNo.eq(sales.stoNo))
                .leftJoin(product)
                .on(stock.proCd.eq(product.proCd))
                .leftJoin(consumer)
                .on(sales.csmCd.eq(consumer.csmCd))
                .where(searchByTextInput(req))
                .orderBy(sales.salesNo.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = jpaQueryFactory.select(sales.count())
                .from(sales)
                .leftJoin(stock)
                .on(stock.stoNo.eq(sales.stoNo))
                .leftJoin(product)
                .on(stock.proCd.eq(product.proCd))
                .leftJoin(consumer)
                .on(sales.csmCd.eq(consumer.csmCd))
                .where(searchByTextInput(req))
                .fetchOne();

        return new PageImpl<>(list, pageable, totalCount != null ? totalCount : 0L);
    }

    private BooleanExpression searchByTextInput(SalesListReq req){
        BooleanExpression searchExpression = null;
        String outWy = req.getOutWy();
        String searchWord = req.getSearchWord();
        String startDt = req.getStartDt();
        String endDt = req.getEndDt();

        if (!StringUtils.hasText(outWy))    outWy = "ALL";
        if (!StringUtils.hasText(searchWord))    searchWord = "";
        if(StringUtils.hasText(startDt)) {
            startDt += " 00:00:00";
        } else {
            startDt = "";
        }
        if(StringUtils.hasText(endDt)) {
            endDt += " 23:59:59";
        } else {
            endDt = "";
        }
        log.info("### startDt: " + startDt);
        log.info("### endDt: " + endDt);

        if (!outWy.equals("ALL")) {
            if(!searchWord.equals("")) {
                if(startDt.equals("") && endDt.equals("")) {
                    searchExpression = sales.outWy.eq(outWy).and(consumer.csmNm.contains(searchWord));
                } else if(!startDt.equals("") && endDt.equals("")) {
                    searchExpression = sales.regDt.gt(startDt).and(sales.outWy.eq(outWy)).and(consumer.csmNm.contains(searchWord));
                } else if(startDt.equals("") && !endDt.equals("")) {
                    searchExpression = sales.regDt.lt(endDt).and(sales.outWy.eq(outWy)).and(consumer.csmNm.contains(searchWord));
                } else {
                    searchExpression = sales.regDt.gt(startDt).and(sales.regDt.lt(endDt)).and(sales.outWy.eq(outWy)).and(consumer.csmNm.contains(searchWord));
                }
            } else {
                if(startDt.equals("") && endDt.equals("")) {
                    searchExpression = sales.outWy.eq(outWy);
                } else if(!startDt.equals("") && endDt.equals("")) {
                    searchExpression = sales.regDt.gt(startDt).and(sales.outWy.eq(outWy));
                } else if(startDt.equals("") && !endDt.equals("")) {
                    searchExpression = sales.regDt.lt(endDt).and(sales.outWy.eq(outWy));
                } else {
                    searchExpression = sales.regDt.gt(startDt).and(sales.regDt.lt(endDt)).and(sales.outWy.eq(outWy));
                }
            }
        } else if(!searchWord.equals("")) {
            if(startDt.equals("") && endDt.equals("")) {
                searchExpression = consumer.csmNm.contains(searchWord);
            } else if(!startDt.equals("") && endDt.equals("")) {
                searchExpression = sales.regDt.gt(startDt).and(consumer.csmNm.contains(searchWord));
            } else if(startDt.equals("") && !endDt.equals("")) {
                searchExpression = sales.regDt.lt(endDt).and(consumer.csmNm.contains(searchWord));
            } else {
                searchExpression = sales.regDt.gt(startDt).and(sales.regDt.lt(endDt)).and(consumer.csmNm.contains(searchWord));
            }
        } else {
            if(startDt.equals("") && endDt.equals("")) {
                log.info("### no date specified");
            } else if(!startDt.equals("") && endDt.equals("")) {
                searchExpression = sales.regDt.gt(startDt);
            } else if(startDt.equals("") && !endDt.equals("")) {
                searchExpression = sales.regDt.lt(endDt);
            } else {
                searchExpression = sales.regDt.gt(startDt).and(sales.regDt.lt(endDt));
            }
        }

        return searchExpression;
    }

    public List<SalesListRes> findSalesForExcel(SalesListReq req) {
        log.info("### findSalesForExcel");

        List<SalesListRes> list = jpaQueryFactory
                .select(Projections.fields(SalesListRes.class,
                        sales.salesNo,
                        stock.stoNo,
                        stock.lotNo,
                        stock.expDt,
                        product.proNm,
                        stock.fromStorage,
                        sales.outCnt,
                        sales.outWy,
                        consumer.csmNm,
                        sales.memo,
                        sales.regId,
                        sales.regDt,
                        sales.modId,
                        sales.modDt
                )).from(sales)
                .leftJoin(stock)
                .on(stock.stoNo.eq(sales.stoNo))
                .leftJoin(product)
                .on(stock.proCd.eq(product.proCd))
                .leftJoin(consumer)
                .on(sales.csmCd.eq(consumer.csmCd))
                .where(searchByTextInput(req))
                .orderBy(sales.salesNo.desc())
                .fetch();
        return list;
    }

    @Transactional
    @Override
    public String deleteSalesBySalesNo(Map<String, Object> params) {

        String salesNo = String.valueOf(params.get("sales_no"));

        // vvv product 테이블의 제품수량을 조정
        adjustStockByDelete(salesNo);

        try {
            long result = jpaQueryFactory
                    .delete(sales)
                    .where(sales.salesNo.eq(Long.parseLong(salesNo)))
                    .execute();

            log.info("deleteSalesBySalesNo ==> DELETE 성공 - result: {}", result);
            return String.valueOf(result);

        } catch (Exception e) {
            log.info("deleteSalesBySalesNo ==> DELETE 실패 - 에러: {}", e);
            return "error_04";
        }

//        return String.valueOf(jpaQueryFactory
//                .delete(sales)
//                .where(sales.salesNo.eq(Long.parseLong(salesNo)))
//                .execute());
    }

    //삭제 시 storage 갯수를 조정
    public String adjustStockByDelete(String salesNo) {

        Long stoNo = jpaQueryFactory
                .select(sales.stoNo)
                .from(sales)
                .where(sales.salesNo.eq(Long.parseLong(salesNo)))
                .fetchOne();
        Long outCnt = jpaQueryFactory
                .select(sales.outCnt)
                .from(sales)
                .where(sales.salesNo.eq(Long.parseLong(salesNo)))
                .fetchOne();

        // stock 테이블 rest_cnt 현행화
        Long restCnt = jpaQueryFactory
                .select(stock.restCnt)
                .from(stock)
                .where(stock.stoNo.eq(stoNo))
                .fetchOne();

        restCnt += outCnt;

        try {
            int result = entityManager
                    .createNativeQuery("UPDATE stock SET REST_CNT = ? WHERE STO_NO = ?")
                    .setParameter(1, restCnt)
                    .setParameter(2, stoNo)
                    .executeUpdate();
            log.info("adjustStockByDelete ==> UPDATE stock 성공 - result: {}", result);
        } catch (Exception e) {
            log.info("adjustStockByDelete ==> UPDATE stock 실패 - 에러: {}", e);
        }

//        String.valueOf(entityManager
//        .createNativeQuery("UPDATE stock SET REST_CNT = ? WHERE STO_NO = ?")
//        .setParameter(1, restCnt)
//        .setParameter(2, stoNo)
//        .executeUpdate());

        // product 테이블 hqStorage, firstStorage 현행화
        String proCd = jpaQueryFactory
                .select(stock.proCd)
                .from(stock)
                .where(stock.stoNo.eq(stoNo))
                .fetchOne();
        String fromStorage = jpaQueryFactory
                .select(stock.fromStorage)
                .from(stock)
                .where(stock.stoNo.eq(stoNo))
                .fetchOne();
        Long hqStorage = jpaQueryFactory
                .select(product.hqStorage)
                .from(product)
                .where(product.proCd.eq(proCd))
                .fetchOne();
        Long firstStorage = jpaQueryFactory
                .select(product.firstStorage)
                .from(product)
                .where(product.proCd.eq(proCd))
                .fetchOne();

        if (fromStorage.equals("HQ")) {
            hqStorage += outCnt;
        } else if (fromStorage.equals("FIRST")) {
            firstStorage += outCnt;
        }

        try {
            int result = entityManager
                    .createNativeQuery("UPDATE product SET HQ_STORAGE = ?, FIRST_STORAGE = ? WHERE PRO_CD = ?")
                    .setParameter(1, hqStorage)
                    .setParameter(2, firstStorage)
                    .setParameter(3, proCd)
                    .executeUpdate();
            log.info("adjustStockByDelete ==> UPDATE product 성공 - result: {}", result);
            return String.valueOf(result);
        } catch (Exception e) {
            log.info("adjustStockByDelete ==> UPDATE product 실패 - 에러: {}", e);
            return "error_05";
        }

//        return String.valueOf(entityManager
//                .createNativeQuery("UPDATE product SET HQ_STORAGE = ?, FIRST_STORAGE = ? WHERE PRO_CD = ?")
//                .setParameter(1, hqStorage)
//                .setParameter(2, firstStorage)
//                .setParameter(3, proCd)
//                .executeUpdate());
    }


    @Override
    public Page<SalesListRes> getStatsaleListBySearchDtoAndPageable(StatsaleListReq req, Pageable pageable) {
        log.info("### getListBySearchDtoAndPageable");

        List<SalesListRes> list = jpaQueryFactory
                .select(Projections.fields(SalesListRes.class,
                        sales.salesNo,
                        stock.stoNo,
                        stock.lotNo,
                        stock.expDt,
                        product.proNm,
                        stock.fromStorage,
                        sales.outCnt,
                        sales.outWy,
                        consumer.csmNm,
                        sales.memo,
                        sales.regId,
                        sales.regDt,
                        sales.modId,
                        sales.modDt
                )).from(sales)
                .leftJoin(stock)
                .on(stock.stoNo.eq(sales.stoNo))
                .leftJoin(product)
                .on(stock.proCd.eq(product.proCd))
                .leftJoin(consumer)
                .on(sales.csmCd.eq(consumer.csmCd))
                .where(searchByInput(req))
                .orderBy(sales.salesNo.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = jpaQueryFactory.select(sales.count())
                .from(sales)
                .leftJoin(stock)
                .on(stock.stoNo.eq(sales.stoNo))
                .leftJoin(product)
                .on(stock.proCd.eq(product.proCd))
                .leftJoin(consumer)
                .on(sales.csmCd.eq(consumer.csmCd))
                .where(searchByInput(req))
                .fetchOne();

        return new PageImpl<>(list, pageable, totalCount != null ? totalCount : 0L);
    }

    private BooleanExpression searchByInput(StatsaleListReq req){
        log.info("### searchByInput");
        //private BooleanExpression searchByInput(SearchReqDto req){
        BooleanExpression searchExpression = null;
        //String outWy = req.getOutWy();
        String searchWord = req.getSearchWord();
        String startDt = req.getStartDt();
        String endDt = req.getEndDt();

        //if (!StringUtils.hasText(outWy))    outWy = "ALL";
        if (!StringUtils.hasText(searchWord))    searchWord = "";
        if(StringUtils.hasText(startDt)) {
            startDt += " 00:00:00";
        } else {
            startDt = "";
        }
        if(StringUtils.hasText(endDt)) {
            endDt += " 23:59:59";
        } else {
            endDt = "";
        }
        log.info("### startDt: " + startDt);
        log.info("### endDt: " + endDt);

        return searchExpression;
    }
}
