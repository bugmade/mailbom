package com.sweetk.cso.repository.custom.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sweetk.cso.dto.StockListReq;
import com.sweetk.cso.dto.StockListRes;
import com.sweetk.cso.dto.common.SearchReqDto;
import com.sweetk.cso.entity.QConsumer;
import com.sweetk.cso.entity.Stock;
import com.sweetk.cso.repository.custom.StockCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.sweetk.cso.entity.QConsumer.consumer;
import static com.sweetk.cso.entity.QProduct.product;
import static com.sweetk.cso.entity.QStock.stock;

@Log4j2
@Repository
@RequiredArgsConstructor
public class StockCustomRepositoryImpl implements StockCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager entityManager;

    @Override
    public Page<StockListRes> getListBySearchDtoAndPageable(StockListReq req, Pageable pageable) {
        log.info("### getListBySearchDtoAndPageable");

        List<StockListRes> list = jpaQueryFactory
                .select(Projections.fields(StockListRes.class,
                        stock.stoNo,
                        stock.proCd,
                        product.proNm,
                        stock.inOut,
                        stock.ioCnt,
                        stock.outWy,
                        consumer.csmNm,
                        stock.memo,
                        stock.RegId,
                        stock.RegDt,
                        stock.ModId,
                        stock.ModDt
                )).from(stock)
                .leftJoin(product)
                .on(stock.proCd.eq(product.proCd))
                .leftJoin(consumer)
                .on(stock.csmCd.eq(consumer.csmCd))
                .where(searchByTextInput(req))
                .orderBy(stock.stoNo.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = jpaQueryFactory.select(stock.count())
                .from(stock)
                .leftJoin(product)
                .on(stock.proCd.eq(product.proCd))
                .leftJoin(consumer)
                .on(stock.csmCd.eq(consumer.csmCd))
                .where(searchByTextInput(req))
                .fetchOne();

        return new PageImpl<>(list, pageable, totalCount != null ? totalCount : 0L);
    }

    private BooleanExpression searchByTextInput(SearchReqDto req){
        BooleanExpression searchExpression = null;
        String inOut = req.getInOut();
        String outWy = req.getOutWy();
        String searchWord = req.getSearchWord();

        if (!StringUtils.hasText(inOut))    inOut = "ALL";
        if (!StringUtils.hasText(outWy))    outWy = "ALL";
        if (!StringUtils.hasText(searchWord))    searchWord = "";

        log.info("### searchByTextInput [inOut:" + inOut + ", outWy:" + outWy + ", searchWord:" + searchWord + "]");

        // 입고일때
        if (inOut.equals("IN")) {
            searchExpression = stock.inOut.eq(inOut);
        }
        else if (inOut.equals("OUT")) {    // 출고일때
            if (!outWy.equals("ALL")) {
                if(outWy.equals("SALE") && !searchWord.equals(""))
                    searchExpression = stock.inOut.eq(inOut).and(stock.outWy.eq(outWy)).and(consumer.csmNm.eq(searchWord));
                else
                    searchExpression = stock.inOut.eq(inOut).and(stock.outWy.eq(outWy));
            } else {
                searchExpression = stock.inOut.eq(inOut);
            }
        }
        return searchExpression;
    }

    @Override
    public List<StockListRes> findAllStock() {
        log.info("### findAllStock");

        // [JPA] QueryDSL
        return jpaQueryFactory
                .select(Projections.fields(StockListRes.class,
                        stock.stoNo,
                        stock.proCd,
                        product.proNm,
                        stock.inOut,
                        stock.ioCnt,
                        stock.outWy,
                        consumer.csmNm,
                        stock.memo,
                        stock.RegId,
                        stock.RegDt,
                        stock.ModId,
                        stock.ModDt
                        ))
                .from(stock)
                .leftJoin(product)
                .on(stock.proCd.eq(product.proCd))
                .leftJoin(consumer)
                .on(stock.csmCd.eq(consumer.csmCd))
                //.where(product.csoNm.contains(csoNm))
                .orderBy(stock.stoNo.desc())
                .fetch();
    }

    public List<StockListRes> findStockForExcel(StockListReq req) {
        log.info("### findStockForExcel");

//        List<StockListRes> resultList = entityManager
//                .createNativeQuery("SELECT PRO_CD, IN_OUT, IO_CNT, OUT_WY, CSM_CD, MEMO, REG_ID, REG_DT FROM stock")
//                //.setParameter("name", "le")
//                .getResultList();
//        log.info(resultList);


        List<StockListRes> list = jpaQueryFactory
                .select(Projections.fields(StockListRes.class,
                        stock.stoNo,
                        stock.proCd,
                        product.proNm,
                        stock.inOut,
                        stock.ioCnt,
                        stock.outWy,
                        consumer.csmNm,
                        stock.memo,
                        stock.RegId,
                        stock.RegDt,
                        stock.ModId,
                        stock.ModDt
                )).from(stock)
                .leftJoin(product)
                .on(stock.proCd.eq(product.proCd))
                .leftJoin(consumer)
                .on(stock.csmCd.eq(consumer.csmCd))
                .where(searchByTextInput(req))
                .orderBy(stock.stoNo.desc())
                .fetch();
        return list;
    }

    @Override
    public Stock findStockByProCd(String proCd) {
        log.info("### findStockByProCd");
        log.info(proCd);
        return jpaQueryFactory
                .selectFrom(stock)
                .where(stock.proCd.eq(proCd))
                .fetchOne();
    }

    @Transactional
    @Override
    public String createStock(Map<String, Object> params) {
        Date now = new Date();

        log.info("### createStock");
        log.info(params);
        Long ioCnt = Long.parseLong(String.valueOf(params.get("io_cnt")));
        log.info(ioCnt);

        // vvv product 테이블의 제품수량을 조정
        Long proSt = jpaQueryFactory
                .select(product.proSt)
                .from(product)
                .where(product.proCd.eq(String.valueOf(params.get("pro_cd"))))
                .fetchOne();

        // product 테이블에 해당 제품이 있을때만 업데이트 수행
        if(proSt != null) {
            log.info("### before proSt:" + proSt);

            if (String.valueOf(params.get("in_out")).equals("IN")) {  // 입고
                proSt = proSt + ioCnt;
            } else {    // 출고
                proSt = proSt - ioCnt;
                if (proSt < 0) proSt = 0L;
            }

            log.info("### after proSt:" + proSt);

            String.valueOf(entityManager
                    .createNativeQuery("UPDATE product SET PRO_ST = ? WHERE PRO_CD = ?")
                    .setParameter(1, proSt)
                    .setParameter(2, String.valueOf(params.get("pro_cd")))
                    .executeUpdate());
        }

        return String.valueOf(entityManager
                .createNativeQuery("INSERT INTO stock (PRO_CD, IN_OUT, IO_CNT, OUT_WY, CSM_CD, MEMO, REG_ID, REG_DT) VALUES (?,?,?,?,?,?,?,?)")
                .setParameter(1, String.valueOf(params.get("pro_cd")))
                .setParameter(2, String.valueOf(params.get("in_out")))
                .setParameter(3, ioCnt)
                .setParameter(4, String.valueOf(params.get("out_wy")))
                .setParameter(5, String.valueOf(params.get("csm_cd")))
                .setParameter(6, String.valueOf(params.get("memo")))
                .setParameter(7, String.valueOf(params.get("login_id")))
                .setParameter(8, now)
                .executeUpdate());
    }

    @Transactional
    @Override
    public String deleteStockByStoNo(String stoNo) {
        log.info("### deleteStockByStoNo");
        log.info(stoNo);

        // vvv product 테이블의 제품수량을 조정

        String proCd = jpaQueryFactory
                .select(stock.proCd)
                .from(stock)
                .where(stock.stoNo.eq(Long.parseLong(stoNo)))
                .fetchOne();

        Long proSt = jpaQueryFactory
                .select(product.proSt)
                .from(product)
                .where(product.proCd.eq(String.valueOf(proCd)))
                .fetchOne();

        // vvv product 테이블에 해당 제품이 존재할때만 업데이트 수행
        if(proSt != null) {
            String inOut = jpaQueryFactory
                    .select(stock.inOut)
                    .from(stock)
                    .where(stock.stoNo.eq(Long.parseLong(stoNo)))
                    .fetchOne();
            Long ioCnt = jpaQueryFactory
                    .select(stock.ioCnt)
                    .from(stock)
                    .where(stock.stoNo.eq(Long.parseLong(stoNo)))
                    .fetchOne();

            log.info("### before proSt:" + proSt);

            if (inOut.equals("IN")) {  // 입고
                proSt = proSt - ioCnt;
                if (proSt < 0) proSt = 0L;
            } else {    // 출고
                proSt = proSt + ioCnt;
            }

            log.info("### after proSt:" + proSt);

            String.valueOf(entityManager
                    .createNativeQuery("UPDATE product SET PRO_ST = ? WHERE PRO_CD = ?")
                    .setParameter(1, proSt)
                    .setParameter(2, proCd)
                    .executeUpdate());
        }

        // ^^^ product 테이블의 제품수량을 조정

        return String.valueOf(jpaQueryFactory
                .delete(stock)
                .where(stock.stoNo.eq(Long.parseLong(stoNo)))
                .execute());
    }
    @Override
    public Page<Stock> findPageAllByProNm(String proNm, Pageable pageable) {
        Long totCnt = jpaQueryFactory
                .select(stock.count())
                .from(stock)
                .where(stock.proCd.contains(proNm))
                .fetchOne();

        List<Stock> stockList = jpaQueryFactory
                .select(stock)
                .from(stock)
                .where(stock.proCd.contains(proNm))
                .orderBy(stock.proCd.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(stockList, pageable, totCnt != null ? totCnt : 0L);
    }
}
