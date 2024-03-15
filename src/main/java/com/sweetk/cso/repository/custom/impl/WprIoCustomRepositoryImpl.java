package com.sweetk.cso.repository.custom.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sweetk.cso.dto.WprIoListReq;
import com.sweetk.cso.dto.WprIoListRes;
import com.sweetk.cso.dto.WrapperListReq;
import com.sweetk.cso.dto.WrapperListRes;
import com.sweetk.cso.dto.common.SearchReqDto;
import com.sweetk.cso.entity.WprIo;
import com.sweetk.cso.entity.Wrapper;
import com.sweetk.cso.repository.custom.WprIoCustomRepository;
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

import static com.sweetk.cso.entity.QWrapper.wrapper;
import static com.sweetk.cso.entity.QWprIo.wprIo;

@Log4j2
@Repository
@RequiredArgsConstructor
public class WprIoCustomRepositoryImpl implements WprIoCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager entityManager;
    final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Page<WprIoListRes> getListBySearchDtoAndPageable(WprIoListReq req, Pageable pageable) {
        log.info("### getListBySearchDtoAndPageable");

        List<WprIoListRes> list = jpaQueryFactory
                .select(Projections.fields(WprIoListRes.class,
                        wprIo.wprioNo,
                        wprIo.wprNo,
                        wprIo.expDt,
                        wprIo.regId,
                        wprIo.regDt
                )).from(wprIo)
                .where(searchByTextInput(req))
                .orderBy(wprIo.wprioNo.desc())
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
    public List<WprIo> findAll() {
        return jpaQueryFactory
                .selectFrom(wprIo)
                //.where(product.csoNm.contains(csoNm))
                //.orderBy(product.csoCd.asc())
                .fetch();
    }

//    @Override
//    public WprIo readWrapperDetail(Map<String, Object> params) {
//        log.info("### readWrapperDetail");
//        Long wprioNo = Long.parseLong(String.valueOf(params.get("wprio_no")));
//
//        return jpaQueryFactory
//                .selectFrom(wprIo)
//                .where(wprIo.wprioNo.eq(wprioNo))
//                .fetchOne();
//    }

    public WprIo findWrapperByWprNm(String wprNm) {
        log.info("### findWrapperByWprNm");

        Long temp = Long.parseLong(wprNm);

        return jpaQueryFactory
                .selectFrom(wprIo)
                .where(wprIo.wprNo.eq(temp))
                .fetchOne();
    }

//    @Transactional
//    @Override
//    public String createWrapper(Map<String, Object> params) {
//        log.info("### createWrapper");
//        log.info(params);
//
//        // 이미 존재하는지 체크
//        Wrapper wrapper = findWrapperByWprNm(String.valueOf(params.get("wpr_nm")));
//        if(wrapper != null) {
//            log.info("### already exist");
//            return "0";
//        }
//
//        Date now = new Date();
//        Long hqStorage = Long.parseLong(String.valueOf(params.get("hq_storage")));
//
//        return String.valueOf(entityManager
//                .createNativeQuery("INSERT INTO wrapper (WPR_NM, WPR_DT, HQ_STORAGE, REG_ID, REG_DT) VALUES (?,?,?,?,?)")
//                .setParameter(1, String.valueOf(params.get("wpr_nm")))
//                .setParameter(2, String.valueOf(params.get("wpr_dt")))
//                .setParameter(3, hqStorage)
//                .setParameter(4, String.valueOf(params.get("login_id")))
//                .setParameter(5, now)
//                .executeUpdate());
//    }
//
//    @Transactional
//    @Override
//    public String updateWrapper(Map<String, Object> params) {
//        Date now = new Date();
//
//        log.info("### updateWrapper");
//        log.info(params);
//        Long wprNo = Long.parseLong(String.valueOf(params.get("wpr_no")));
//        Long hqStorage = Long.parseLong(String.valueOf(params.get("hq_storage")));
//
//        return String.valueOf(entityManager
//                .createNativeQuery("UPDATE wrapper SET WPR_NM = ?, WPR_DT = ?, HQ_STORAGE = ?, MOD_ID = ?, MOD_DT = ? WHERE WPR_NO = ?")
//                .setParameter(1, String.valueOf(params.get("wpr_nm")))
//                .setParameter(2, String.valueOf(params.get("wpr_dt")))
//                .setParameter(3, hqStorage)
//                .setParameter(4, String.valueOf(params.get("login_id")))
//                .setParameter(5, now)
//                .setParameter(6, wprNo)
//                .executeUpdate());
//    }
//
//    @Transactional
//    @Override
//    public String deleteWrapper(Map<String, Object> params) {
//        log.info("### deleteWrapper");
//
//        Long wprNo = Long.parseLong(String.valueOf(params.get("wpr_no")));
//        return String.valueOf(jpaQueryFactory
//                .delete(wrapper)
//                .where(wrapper.wprNo.eq(wprNo))
//                .execute());
//    }

//    @Override
//    public Page<Product> findPageAllByProNm(String proNm, Pageable pageable) {
//        Long totCnt = jpaQueryFactory
//                .select(product.count())
//                .from(product)
//                .where(product.proNm.contains(proNm))
//                .fetchOne();
//
//        List<Product> productList = jpaQueryFactory
//                .select(product)
//                .from(product)
//                .where(product.proNm.contains(proNm))
//                .orderBy(product.proCd.asc())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        return new PageImpl<>(productList, pageable, totCnt != null ? totCnt : 0L);
//    }
}
