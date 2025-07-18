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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.sweetk.cso.entity.QAdm.adm;
import static com.sweetk.cso.entity.QConsumer.consumer;
import static com.sweetk.cso.entity.QProduct.product;
import static com.sweetk.cso.entity.QStock.stock;

@Log4j2
@Repository
@RequiredArgsConstructor
public class StockCustomRepositoryImpl implements StockCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager entityManager;
    final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Page<StockListRes> getListBySearchDtoAndPageable(StockListReq req, Pageable pageable) {
        log.info("### getListBySearchDtoAndPageable");

        String proCd = req.getProCd();
        if (!StringUtils.hasText(proCd)) proCd = "ALL";

        //@@@ 맘에들지않음
        if (proCd.equals("ALL")) {
            List<StockListRes> list = jpaQueryFactory
                    .select(Projections.fields(StockListRes.class,
                            stock.stoNo,
                            stock.proCd,
                            product.proNm,
                            stock.inOut,
                            stock.ioCnt,
                            stock.restCnt,
                            product.bongBox,
                            stock.fromStorage,
                            stock.lotNo,
                            stock.expDt,
                            stock.memo,
                            stock.regId,
                            stock.regDt,
                            stock.modId,
                            stock.modDt
                    )).from(stock)
                    .leftJoin(product)
                    .on(stock.proCd.eq(product.proCd))
                    .where(searchByTextInput(req))
                    .orderBy(stock.stoNo.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
            Long totalCount = jpaQueryFactory.select(stock.count())
                    .from(stock)
                    .leftJoin(product)
                    .on(stock.proCd.eq(product.proCd))
                    .where(searchByTextInput(req))
                    .fetchOne();

            return new PageImpl<>(list, pageable, totalCount != null ? totalCount : 0L);

        } else {
            List<StockListRes> list = jpaQueryFactory
                    .select(Projections.fields(StockListRes.class,
                            stock.stoNo,
                            stock.proCd,
                            product.proNm,
                            stock.inOut,
                            stock.ioCnt,
                            stock.restCnt,
                            product.bongBox,
                            stock.fromStorage,
                            stock.lotNo,
                            stock.expDt,
                            stock.memo,
                            stock.regId,
                            stock.regDt,
                            stock.modId,
                            stock.modDt
                    )).from(stock)
                    .leftJoin(product)
                    .on(stock.proCd.eq(product.proCd))
                    .where(searchByTextInput(req))
                    .orderBy(stock.expDt.asc(), stock.lotNo.asc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
            Long totalCount = jpaQueryFactory.select(stock.count())
                    .from(stock)
                    .leftJoin(product)
                    .on(stock.proCd.eq(product.proCd))
                    .where(searchByTextInput(req))
                    .fetchOne();

            return new PageImpl<>(list, pageable, totalCount != null ? totalCount : 0L);
        }
    }

    private BooleanExpression searchByTextInput(StockListReq req){
        BooleanExpression searchExpression = null;
        String proCd = req.getProCd();
        String searchWord = req.getSearchWord();

        if (!StringUtils.hasText(proCd))    proCd = "ALL";
        if (!StringUtils.hasText(searchWord))    searchWord = "";

        log.info("### searchByTextInput [proCd:" + proCd + ", searchWord:" + searchWord + "]");

        if (!proCd.equals("ALL")) {
            searchExpression = stock.proCd.eq(proCd).and(stock.restCnt.gt(0L));
        } else {
            searchExpression = stock.restCnt.gt(0L);
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
                        stock.restCnt,
                        product.bongBox,
                        stock.fromStorage,
                        stock.lotNo,
                        stock.expDt,
                        stock.memo,
                        stock.regId,
                        stock.regDt,
                        stock.modId,
                        stock.modDt
                        ))
                .from(stock)
                .leftJoin(product)
                .on(stock.proCd.eq(product.proCd))
//                .leftJoin(consumer)
//                .on(stock.csmCd.eq(consumer.csmCd))
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
                        stock.restCnt,
                        product.bongBox,
                        stock.fromStorage,
                        stock.lotNo,
                        stock.expDt,
                        stock.memo,
                        stock.regId,
                        stock.regDt,
                        stock.modId,
                        stock.modDt
                )).from(stock)
                .leftJoin(product)
                .on(stock.proCd.eq(product.proCd))
                .where(searchByTextInputExcel(req))
                .orderBy(stock.stoNo.desc())
                .fetch();
        return list;
    }

    private BooleanExpression searchByTextInputExcel(StockListReq req){
        BooleanExpression searchExpression = null;
        String proCd = req.getProCd();
        String searchWord = req.getSearchWord();

        if (!StringUtils.hasText(proCd))    proCd = "ALL";
        if (!StringUtils.hasText(searchWord))    searchWord = "";

        log.info("### searchByTextInput [proCd:" + proCd + ", searchWord:" + searchWord + "]");

        if (!proCd.equals("ALL")) {
            searchExpression = stock.proCd.eq(proCd);
        }
        return searchExpression;
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
        log.info("### createStock");
        log.info(params);

        Date now = new Date();
        String inOut = String.valueOf(params.get("in_out"));

        if(inOut.equals("IN")) {
            adjustStorageByIn(params);
            try {
                int result = entityManager
                    .createNativeQuery("INSERT INTO stock (PRO_CD, IN_OUT, IO_CNT, REST_CNT, FROM_STORAGE, LOT_NO, EXP_DT, MEMO, REG_ID, REG_DT) VALUES (?,?,?,?,?,?,?,?,?,?)")
                    .setParameter(1, String.valueOf(params.get("pro_cd")))
                    .setParameter(2, inOut)
                    .setParameter(3, Long.parseLong(String.valueOf(params.get("io_cnt"))))
                    .setParameter(4, Long.parseLong(String.valueOf(params.get("io_cnt"))))
                    .setParameter(5, String.valueOf(params.get("from_storage")))
                    .setParameter(6, String.valueOf(params.get("lot_no")))
                    .setParameter(7, String.valueOf(params.get("exp_dt")))
                    .setParameter(8, String.valueOf(params.get("memo")))
                    .setParameter(9, String.valueOf(params.get("login_id")))
                    .setParameter(10, now)
                    .executeUpdate();
                log.info("createStock IN ==> INSERT 성공 - result: {}", result);
                return String.valueOf(result);
            } catch (Exception e) {
                log.info("createStock IN ==> INSERT 실패 - 에러: {}", e);
                return "error_01";
            }
//            return String.valueOf(entityManager
//                    .createNativeQuery("INSERT INTO stock (PRO_CD, IN_OUT, IO_CNT, REST_CNT, FROM_STORAGE, LOT_NO, EXP_DT, MEMO, REG_ID, REG_DT) VALUES (?,?,?,?,?,?,?,?,?,?)")
//                    .setParameter(1, String.valueOf(params.get("pro_cd")))
//                    .setParameter(2, inOut)
//                    .setParameter(3, Long.parseLong(String.valueOf(params.get("io_cnt"))))
//                    .setParameter(4, Long.parseLong(String.valueOf(params.get("io_cnt"))))
//                    .setParameter(5, String.valueOf(params.get("from_storage")))
//                    .setParameter(6, String.valueOf(params.get("lot_no")))
//                    .setParameter(7, String.valueOf(params.get("exp_dt")))
//                    .setParameter(8, String.valueOf(params.get("memo")))
//                    .setParameter(9, String.valueOf(params.get("login_id")))
//                    .setParameter(10, now)
//                    .executeUpdate());
        } else if(inOut.equals("TRANSFER")) {
            adjustStorageByTransfer(params);

            try {
                int result = entityManager
                        .createNativeQuery("UPDATE stock SET IN_OUT = ?, FROM_STORAGE = ?, MOD_ID = ?, MOD_DT = ? WHERE STO_NO = ?")
                        .setParameter(1, "TRANSFER")
                        .setParameter(2, String.valueOf(params.get("to_storage")))
                        .setParameter(3, String.valueOf(params.get("login_id")))
                        .setParameter(4, now)
                        .setParameter(5, Long.parseLong(String.valueOf(params.get("sto_no"))))
                        .executeUpdate();
                log.info("createStock TRANSFER ==> UPDATE 성공 - result: {}", result);
                return String.valueOf(result);
            } catch (Exception e) {
                log.info("createStock TRANSFER ==> UPDATE 실패 - 에러: {}", e);
                return "error_02";
            }
//            return String.valueOf(entityManager
//                    .createNativeQuery("UPDATE stock SET IN_OUT = ?, FROM_STORAGE = ?, MOD_ID = ?, MOD_DT = ? WHERE STO_NO = ?")
//                    .setParameter(1, "TRANSFER")
//                    .setParameter(2, String.valueOf(params.get("to_storage")))
//                    .setParameter(3, String.valueOf(params.get("login_id")))
//                    .setParameter(4, now)
//                    .setParameter(5, Long.parseLong(String.valueOf(params.get("sto_no"))))
//                    .executeUpdate());
        } else {
            adjustStorageByOut(params);

            //잔여수량 조정
            Long io_cnt = Long.parseLong(String.valueOf(params.get("io_cnt")));
            Long restCnt = jpaQueryFactory
                    .select(stock.restCnt)
                    .from(stock)
                    .where(stock.stoNo.eq(Long.parseLong(String.valueOf(params.get("sto_no")))))
                    .fetchOne();
            restCnt -= io_cnt;
            if(restCnt < 0) restCnt = 0L;

            try {
                int result = entityManager
                    .createNativeQuery("UPDATE stock SET REST_CNT = ?, MOD_ID = ?, MOD_DT = ? WHERE STO_NO = ?")
                    .setParameter(1, restCnt)
                    .setParameter(2, String.valueOf(params.get("login_id")))
                    .setParameter(3, now)
                    .setParameter(4, Long.parseLong(String.valueOf(params.get("sto_no"))))
                    .executeUpdate();
                log.info("createStock OUT ==> UPDATE 성공 - result: {}", result);
            } catch (Exception e) {
                log.info("createStock OUT ==> UPDATE 실패 - 에러: {}", e);
            }
//            String.valueOf(entityManager
//                    .createNativeQuery("UPDATE stock SET REST_CNT = ?, MOD_ID = ?, MOD_DT = ? WHERE STO_NO = ?")
//                    .setParameter(1, restCnt)
//                    .setParameter(2, String.valueOf(params.get("login_id")))
//                    .setParameter(3, now)
//                    .setParameter(4, Long.parseLong(String.valueOf(params.get("sto_no"))))
//                    .executeUpdate());

            //출고이력 생성
            try {
                int result = entityManager
                        .createNativeQuery("INSERT INTO sales (STO_NO, OUT_CNT, OUT_WY, CSM_CD, MEMO, REG_ID, REG_DT) VALUES (?,?,?,?,?,?,?)")
                        .setParameter(1, String.valueOf(params.get("sto_no")))
                        .setParameter(2, io_cnt)
                        .setParameter(3, String.valueOf(params.get("out_wy")))
                        .setParameter(4, String.valueOf(params.get("csm_cd")))
                        .setParameter(5, String.valueOf(params.get("memo")))
                        .setParameter(6, String.valueOf(params.get("login_id")))
                        .setParameter(7, now)
                        .executeUpdate();
                log.info("createStock OUT ==> INSERT 성공 - result: {}", result);
                return String.valueOf(result);
            } catch (Exception e) {
                log.info("createStock OUT ==> INSERT 실패 - 에러: {}", e);
                return "error_03";
            }
//            return String.valueOf(entityManager
//                    .createNativeQuery("INSERT INTO sales (STO_NO, OUT_CNT, OUT_WY, CSM_CD, MEMO, REG_ID, REG_DT) VALUES (?,?,?,?,?,?,?)")
//                    .setParameter(1, String.valueOf(params.get("sto_no")))
//                    .setParameter(2, io_cnt)
//                    .setParameter(3, String.valueOf(params.get("out_wy")))
//                    .setParameter(4, String.valueOf(params.get("csm_cd")))
//                    .setParameter(5, String.valueOf(params.get("memo")))
//                    .setParameter(6, String.valueOf(params.get("login_id")))
//                    .setParameter(7, now)
//                    .executeUpdate());
        }
    }

    //입고 시 storage 갯수를 조정
    public void adjustStorageByIn(Map<String, Object> params) {
        log.info("### adjustStorageByIn");
        Long io_cnt = Long.parseLong(String.valueOf(params.get("io_cnt")));
        String pro_cd = String.valueOf(params.get("pro_cd"));

        // vvv product 테이블의 현재 재고
        Long hqStorage = jpaQueryFactory
                .select(product.hqStorage)
                .from(product)
                .where(product.proCd.eq(pro_cd))
                .fetchOne();

        hqStorage += io_cnt;  // 입고는 본사창고에만

        try {
            int result = entityManager
                    .createNativeQuery("UPDATE product SET HQ_STORAGE = ? WHERE PRO_CD = ?")
                    .setParameter(1, hqStorage)
                    .setParameter(2, pro_cd)
                    .executeUpdate();
            log.info("adjustStorageByIn ==> UPDATE 성공 - result: {}", result);
        } catch (Exception e) {
            log.info("adjustStorageByIn ==> UPDATE 실패 - 에러: {}", e);
        }

//        String.valueOf(entityManager
//                .createNativeQuery("UPDATE product SET HQ_STORAGE = ? WHERE PRO_CD = ?")
//                .setParameter(1, hqStorage)
//                .setParameter(2, pro_cd)
//                .executeUpdate());
    }

    //창고이동 시 storage 갯수를 조정
    public void adjustStorageByTransfer(Map<String, Object> params) {
        log.info("### adjustStorageByTransfer");
        Long io_cnt = Long.parseLong(String.valueOf(params.get("rest_cnt")));
        String pro_cd = String.valueOf(params.get("pro_cd"));

        // vvv product 테이블의 현재 재고
        Long hqStorage = jpaQueryFactory
                .select(product.hqStorage)
                .from(product)
                .where(product.proCd.eq(pro_cd))
                .fetchOne();
        Long firstStorage = jpaQueryFactory
                .select(product.firstStorage)
                .from(product)
                .where(product.proCd.eq(pro_cd))
                .fetchOne();

        hqStorage -= io_cnt;
        if (hqStorage < 0) hqStorage = 0L;
        firstStorage += io_cnt;

        try {
            int result = entityManager
                    .createNativeQuery("UPDATE product SET HQ_STORAGE = ?, FIRST_STORAGE = ? WHERE PRO_CD = ?")
                    .setParameter(1, hqStorage)
                    .setParameter(2, firstStorage)
                    .setParameter(3, pro_cd)
                    .executeUpdate();
            log.info("adjustStorageByTransfer ==> UPDATE 성공 - result: {}", result);
        } catch (Exception e) {
            log.info("adjustStorageByTransfer ==> UPDATE 실패 - 에러: {}", e);
        }

//        String.valueOf(entityManager
//                .createNativeQuery("UPDATE product SET HQ_STORAGE = ?, FIRST_STORAGE = ? WHERE PRO_CD = ?")
//                .setParameter(1, hqStorage)
//                .setParameter(2, firstStorage)
//                .setParameter(3, pro_cd)
//                .executeUpdate());
    }

    //출고 시 storage 갯수를 조정
    public void adjustStorageByOut(Map<String, Object> params) {
        log.info("### adjustStorageByOut");
        Long io_cnt = Long.parseLong(String.valueOf(params.get("io_cnt")));
        String pro_cd = String.valueOf(params.get("pro_cd"));
        String in_out = String.valueOf(params.get("in_out"));
        String out_wy = String.valueOf(params.get("out_wy"));
        String from_storage = String.valueOf(params.get("from_storage"));
        String to_storage = String.valueOf(params.get("to_storage"));

        // vvv product 테이블의 현재 재고
        Long hqStorage = jpaQueryFactory
                .select(product.hqStorage)
                .from(product)
                .where(product.proCd.eq(pro_cd))
                .fetchOne();
        Long firstStorage = jpaQueryFactory
                .select(product.firstStorage)
                .from(product)
                .where(product.proCd.eq(pro_cd))
                .fetchOne();

        if (from_storage.equals("HQ")) {
            hqStorage -= io_cnt;
        } else if (from_storage.equals("FIRST")) {
            firstStorage -= io_cnt;
        }

        if (hqStorage < 0) hqStorage = 0L;
        if (firstStorage < 0) firstStorage = 0L;

        try {
            int result = entityManager
                    .createNativeQuery("UPDATE product SET HQ_STORAGE = ?, FIRST_STORAGE = ? WHERE PRO_CD = ?")
                    .setParameter(1, hqStorage)
                    .setParameter(2, firstStorage)
                    .setParameter(3, pro_cd)
                    .executeUpdate();
            log.info("adjustStorageByOut ==> UPDATE 성공 - result: {}", result);
        } catch (Exception e) {
            log.info("adjustStorageByOut ==> UPDATE 실패 - 에러: {}", e);
        }

//        String.valueOf(entityManager
//                .createNativeQuery("UPDATE product SET HQ_STORAGE = ?, FIRST_STORAGE = ? WHERE PRO_CD = ?")
//                .setParameter(1, hqStorage)
//                .setParameter(2, firstStorage)
//                .setParameter(3, pro_cd)
//                .executeUpdate());
    }

    @Transactional
    @Override
    public String deleteStockByStoNo(Map<String, Object> params) {
        log.info("### deleteStockByStoNo");

        String stoNo = String.valueOf(params.get("sto_no"));

        // vvv product 테이블의 제품수량을 조정
        adjustStorageByDelete(stoNo);

        try {
            long result = jpaQueryFactory
                    .delete(stock)
                    .where(stock.stoNo.eq(Long.parseLong(stoNo)))
                    .execute();

            log.info("deleteStockByStoNo ==> DELETE 성공 - result: {}", result);
            return String.valueOf(result);

        } catch (Exception e) {
            log.info("deleteStockByStoNo ==> DELETE 실패 - 에러: {}", e);
            return "error_04";
        }

//        return String.valueOf(jpaQueryFactory
//                .delete(stock)
//                .where(stock.stoNo.eq(Long.parseLong(stoNo)))
//                .execute());
    }

    //삭제 시 storage 갯수를 조정
    public void adjustStorageByDelete(String stoNo) {

        // vvv stock, product 테이블의 현재 정보
        String proCd = jpaQueryFactory
                .select(stock.proCd)
                .from(stock)
                .where(stock.stoNo.eq(Long.parseLong(stoNo)))
                .fetchOne();
        Long ioCnt = jpaQueryFactory
                .select(stock.ioCnt)
                .from(stock)
                .where(stock.stoNo.eq(Long.parseLong(stoNo)))
                .fetchOne();
        String fromStorage = jpaQueryFactory
                .select(stock.fromStorage)
                .from(stock)
                .where(stock.stoNo.eq(Long.parseLong(stoNo)))
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
            hqStorage -= ioCnt;
            if (hqStorage < 0) hqStorage = 0L;
        } else if (fromStorage.equals("FIRST")) {
            firstStorage -= ioCnt;
            if (firstStorage < 0) firstStorage = 0L;
        }

        try {
            int result = entityManager
                    .createNativeQuery("UPDATE product SET HQ_STORAGE = ?, FIRST_STORAGE = ? WHERE PRO_CD = ?")
                    .setParameter(1, hqStorage)
                    .setParameter(2, firstStorage)
                    .setParameter(3, proCd)
                    .executeUpdate();
            log.info("adjustStorageByDelete ==> UPDATE 성공 - result: {}", result);
        } catch (Exception e) {
            log.info("adjustStorageByDelete ==> UPDATE 실패 - 에러: {}", e);
        }

//        String.valueOf(entityManager
//                .createNativeQuery("UPDATE product SET HQ_STORAGE = ?, FIRST_STORAGE = ? WHERE PRO_CD = ?")
//                .setParameter(1, hqStorage)
//                .setParameter(2, firstStorage)
//                .setParameter(3, proCd)
//                .executeUpdate());
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
