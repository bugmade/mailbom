package com.sweetk.cso.repository.custom.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sweetk.cso.dto.StockListReq;
import com.sweetk.cso.dto.common.SearchReqDto;
import com.sweetk.cso.dto.pharmComp.PharmCompListReq;
import com.sweetk.cso.dto.pharmComp.PharmCompListRes;
import com.sweetk.cso.repository.custom.PharmCompCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.sweetk.cso.entity.QPharmComp.pharmComp;
import static com.sweetk.cso.entity.QStock.stock;

@Repository
@RequiredArgsConstructor
public class PharmCompCustomRepositoryImpl implements PharmCompCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<PharmCompListRes> getListBySearchDtoAndPageable(PharmCompListReq req, Pageable pageable) {
        List<PharmCompListRes> list = jpaQueryFactory
                .select(Projections.fields(PharmCompListRes.class,
                        pharmComp.pharmCompNo,
                        pharmComp.pharmCompCd,
                        pharmComp.pharmCompNm,
                        pharmComp.bizRegNo,
                        pharmComp.repNm,
                        pharmComp.taxTypeCcd,
                        pharmComp.leastAmt,
                        pharmComp.zipNo,
                        pharmComp.addr,
                        pharmComp.addrDtl
                )).from(pharmComp)
                //.where(searchByTextInput(req))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = jpaQueryFactory.select(pharmComp.count())
                .from(pharmComp)
                //.where(searchByTextInput(req))
                .fetchOne();

        return new PageImpl<>(list, pageable, totalCount != null ? totalCount : 0L);
    }

    private BooleanExpression searchByTextInput(StockListReq req){
        //private BooleanExpression searchByTextInput(SearchReqDto req){
        BooleanExpression searchExpression;
//        String searchType = req.getSearchType();
        String searchType = req.getInOut();
        String searchWord = req.getSearchWord();
        if (!StringUtils.hasText(searchType) || !StringUtils.hasText(searchWord)) {
            searchExpression = null;
        } else {
            searchWord = searchWord.trim();
            searchExpression = switch (searchType){
                case "ALL":
                    yield pharmComp.pharmCompCd.contains(searchWord).or(pharmComp.pharmCompNm.contains(searchWord));
                case "CD":
                    yield pharmComp.pharmCompCd.contains(searchWord);
                case "NM":
                    yield pharmComp.pharmCompNm.contains(searchWord);
                default:
                    yield null;
            };
        }
        return searchExpression;
    }
}
