package com.sweetk.cso.repository.custom.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sweetk.cso.dto.StockListRes;
import com.sweetk.cso.entity.Consumer;
import com.sweetk.cso.entity.Product;
import com.sweetk.cso.entity.Stock;
import com.sweetk.cso.repository.custom.ConsumerCustomRepository;
import com.sweetk.cso.repository.custom.StockCustomRepository;
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

import static com.sweetk.cso.entity.QConsumer.consumer;
import static com.sweetk.cso.entity.QStock.stock;

@Log4j2
@Repository
@RequiredArgsConstructor
public class ConsumerCustomRepositoryImpl implements ConsumerCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager entityManager;

    @Override
    public List<Consumer> findAllConsumer() {
        log.info("### findAllConsumer");

        return jpaQueryFactory
                .selectFrom(consumer)
                .fetch();
    }

    @Override
    public Consumer findConsumerByCsmCd(String csmCd) {
        log.info("### findConsumerByCsmCd");
        log.info(csmCd);
        return jpaQueryFactory
                .selectFrom(consumer)
                .where(consumer.csmCd.eq(csmCd))
                .fetchOne();
    }

    @Transactional
    @Override
    public String createConsumer(Map<String, Object> params) {
        Date now = new Date();

        log.info("### createConsumer");
        log.info(params);

        // CSM_CD가 이미 존재하는지 체크
        Consumer consumer = findConsumerByCsmCd(String.valueOf(params.get("csm_cd")));
        if(consumer != null) {
            log.info("### already exist");
            return "0";
        }

        return String.valueOf(entityManager
                .createNativeQuery("INSERT INTO consumer (CSM_CD, CSM_NM, BIZ_NO, ADDR, PIC_NM, TEL_NO, EMAIL, CSM_DT, REG_ID, REG_DT) VALUES (?,?,?,?,?,?,?,?,?,?)")
                .setParameter(1, String.valueOf(params.get("csm_cd")))
                .setParameter(2, String.valueOf(params.get("csm_nm")))
                .setParameter(3, String.valueOf(params.get("biz_no")))
                .setParameter(4, String.valueOf(params.get("addr")))
                .setParameter(5, String.valueOf(params.get("pic_nm")))
                .setParameter(6, String.valueOf(params.get("tel_no")))
                .setParameter(7, String.valueOf(params.get("email")))
                .setParameter(8, String.valueOf(params.get("csm_dt")))
                .setParameter(9, String.valueOf(params.get("login_id")))
                .setParameter(10, now)
                .executeUpdate());
    }

    @Transactional
    @Override
    public String updateConsumer(Map<String, Object> params) {
        Date now = new Date();

        log.info("### updateConsumer");
        log.info(params);

        return String.valueOf(entityManager
                .createNativeQuery("UPDATE consumer SET CSM_NM = ?, BIZ_NO = ?, ADDR = ?, PIC_NM = ?, TEL_NO = ?, EMAIL = ?, CSM_DT = ?, MOD_ID = ?, MOD_DT = ? WHERE CSM_CD = ?")
                .setParameter(1, String.valueOf(params.get("csm_nm")))
                .setParameter(2, String.valueOf(params.get("biz_no")))
                .setParameter(3, String.valueOf(params.get("addr")))
                .setParameter(4, String.valueOf(params.get("pic_nm")))
                .setParameter(5, String.valueOf(params.get("tel_no")))
                .setParameter(6, String.valueOf(params.get("email")))
                .setParameter(7, String.valueOf(params.get("csm_dt")))
                .setParameter(8, String.valueOf(params.get("login_id")))
                .setParameter(9, now)
                .setParameter(10, String.valueOf(params.get("csm_cd")))
                .executeUpdate());
    }

    @Transactional
    @Override
    public String deleteConsumerByCsmCd(String csmCd) {
        log.info("### deleteConsumerByCsmCd");
        log.info(csmCd);

        return String.valueOf(jpaQueryFactory
                .delete(consumer)
                .where(consumer.csmCd.eq(csmCd))
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
