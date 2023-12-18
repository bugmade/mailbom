package com.sweetk.cso.repository.custom.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sweetk.cso.entity.Cso;
import com.sweetk.cso.repository.custom.CsoCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sweetk.cso.entity.QCso.cso;


@Repository
@RequiredArgsConstructor
public class CsoCustomRepositoryImpl implements CsoCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Cso> findAllByCsoNm(String csoNm) {
        return jpaQueryFactory
                .select(cso)
                .from(cso)
                .where(cso.csoNm.contains(csoNm))
                .orderBy(cso.csoCd.asc())
                .fetch();
    }

    @Override
    public Page<Cso> findPageAllByCsoNm(String csoNm, Pageable pageable) {
        Long totCnt = jpaQueryFactory
                .select(cso.count())
                .from(cso)
                .where(cso.csoNm.contains(csoNm))
                .fetchOne();

        List<Cso> csoList = jpaQueryFactory
                .select(cso)
                .from(cso)
                .where(cso.csoNm.contains(csoNm))
                .orderBy(cso.csoCd.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(csoList, pageable, totCnt != null ? totCnt : 0L);
    }
}
