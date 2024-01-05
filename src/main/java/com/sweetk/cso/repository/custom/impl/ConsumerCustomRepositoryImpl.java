package com.sweetk.cso.repository.custom.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sweetk.cso.dto.*;
import com.sweetk.cso.dto.common.SearchReqDto;
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
public class ConsumerCustomRepositoryImpl implements ConsumerCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager entityManager;
    final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Page<ConsumerListRes> getListBySearchDtoAndPageable(ConsumerListReq req, Pageable pageable) {
        log.info("### getListBySearchDtoAndPageable");

        List<ConsumerListRes> list = jpaQueryFactory
                .select(Projections.fields(ConsumerListRes.class,
                        consumer.csmNo,
                        consumer.csmCd,
                        consumer.csmNm,
                        consumer.bizNo,
                        consumer.addr,
                        consumer.picNm,
                        consumer.telNo,
                        consumer.email,
                        consumer.csmDt,
                        consumer.regId,
                        consumer.regDt
                )).from(consumer)
                .where(searchByTextInput(req))
                .orderBy(consumer.csmNo.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = jpaQueryFactory.select(consumer.count())
                .from(consumer)
                .where(searchByTextInput(req))
                .fetchOne();

        return new PageImpl<>(list, pageable, totalCount != null ? totalCount : 0L);
    }

    private BooleanExpression searchByTextInput(SearchReqDto req){
        BooleanExpression searchExpression = null;
        String searchWord = req.getSearchWord();

        if(StringUtils.hasText(searchWord)) {
            searchExpression = consumer.csmNm.contains(searchWord);
        }
        return searchExpression;
    }

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
    public String deleteConsumerByCsmCd(Map<String, Object> params) {
        log.info("### deleteConsumerByCsmCd");

        String csmCd = String.valueOf(params.get("csm_cd"));

        return String.valueOf(jpaQueryFactory
                .delete(consumer)
                .where(consumer.csmCd.eq(csmCd))
                .execute());
    }
}
