package com.sweetk.cso.repository.custom.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sweetk.cso.dto.WrapperListReq;
import com.sweetk.cso.dto.WrapperListRes;
import com.sweetk.cso.dto.common.SearchReqDto;
import com.sweetk.cso.entity.Product;
import com.sweetk.cso.entity.Wrapper;
import com.sweetk.cso.repository.custom.WrapperCustomRepository;
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

import static com.sweetk.cso.entity.QProduct.product;
import static com.sweetk.cso.entity.QWrapper.wrapper;

@Log4j2
@Repository
@RequiredArgsConstructor
public class WrapperCustomRepositoryImpl implements WrapperCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager entityManager;
    final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Page<WrapperListRes> getListBySearchDtoAndPageable(WrapperListReq req, Pageable pageable) {
        log.info("### getListBySearchDtoAndPageable");

        List<WrapperListRes> list = jpaQueryFactory
                .select(Projections.fields(WrapperListRes.class,
                        wrapper.wprNo,
                        wrapper.wprNm,
                        wrapper.wprDt,
                        wrapper.hqStorage,
                        wrapper.regId,
                        wrapper.regDt
                )).from(wrapper)
                .where(searchByTextInput(req))
                .orderBy(wrapper.wprNo.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = jpaQueryFactory.select(wrapper.count())
                .from(wrapper)
                .where(searchByTextInput(req))
                .fetchOne();

        return new PageImpl<>(list, pageable, totalCount != null ? totalCount : 0L);
    }

    private BooleanExpression searchByTextInput(SearchReqDto req){
        BooleanExpression searchExpression = null;
        String searchWord = req.getSearchWord();

        if(StringUtils.hasText(searchWord)) {
            searchExpression = wrapper.wprNm.contains(searchWord);
        }
        return searchExpression;
    }

    @Override
    public List<Wrapper> findAll() {
        return jpaQueryFactory
                .selectFrom(wrapper)
                //.where(product.csoNm.contains(csoNm))
                //.orderBy(product.csoCd.asc())
                .fetch();
    }

    @Override
    public Wrapper readWrapperDetail(Map<String, Object> params) {
        log.info("### readWrapperDetail");
        Long wprNo = Long.parseLong(String.valueOf(params.get("wpr_no")));

        return jpaQueryFactory
                .selectFrom(wrapper)
                .where(wrapper.wprNo.eq(wprNo))
                .fetchOne();
    }

    public Wrapper findWrapperByWprNm(String wprNm) {
        log.info("### findWrapperByWprNm");
        return jpaQueryFactory
                .selectFrom(wrapper)
                .where(wrapper.wprNm.eq(wprNm))
                .fetchOne();
    }

    @Transactional
    @Override
    public String createWrapper(Map<String, Object> params) {
        log.info("### createWrapper");
        log.info(params);

        // 이미 존재하는지 체크
        Wrapper wrapper = findWrapperByWprNm(String.valueOf(params.get("wpr_nm")));
        if(wrapper != null) {
            log.info("### already exist");
            return "0";
        }

        Date now = new Date();
        Long hqStorage = Long.parseLong(String.valueOf(params.get("hq_storage")));

        return String.valueOf(entityManager
                .createNativeQuery("INSERT INTO wrapper (WPR_NM, WPR_DT, HQ_STORAGE, REG_ID, REG_DT) VALUES (?,?,?,?,?)")
                .setParameter(1, String.valueOf(params.get("wpr_nm")))
                .setParameter(2, String.valueOf(params.get("wpr_dt")))
                .setParameter(3, hqStorage)
                .setParameter(4, String.valueOf(params.get("login_id")))
                .setParameter(5, now)
                .executeUpdate());
    }

    @Transactional
    @Override
    public String updateWrapper(Map<String, Object> params) {
        Date now = new Date();

        log.info("### updateWrapper");
        log.info(params);
        Long wprNo = Long.parseLong(String.valueOf(params.get("wpr_no")));
        Long hqStorage = Long.parseLong(String.valueOf(params.get("hq_storage")));

        return String.valueOf(entityManager
                .createNativeQuery("UPDATE wrapper SET WPR_NM = ?, WPR_DT = ?, HQ_STORAGE = ?, MOD_ID = ?, MOD_DT = ? WHERE WPR_NO = ?")
                .setParameter(1, String.valueOf(params.get("wpr_nm")))
                .setParameter(2, String.valueOf(params.get("wpr_dt")))
                .setParameter(3, hqStorage)
                .setParameter(4, String.valueOf(params.get("login_id")))
                .setParameter(5, now)
                .setParameter(6, wprNo)
                .executeUpdate());
    }

    @Transactional
    @Override
    public String deleteWrapper(Map<String, Object> params) {
        log.info("### deleteWrapper");

        Long wprNo = Long.parseLong(String.valueOf(params.get("wpr_no")));
        return String.valueOf(jpaQueryFactory
                .delete(wrapper)
                .where(wrapper.wprNo.eq(wprNo))
                .execute());
    }

    @Override
    public List<Wrapper> readWrapperList() {
        return jpaQueryFactory
                .selectFrom(wrapper)
                .fetch();
    }
}
