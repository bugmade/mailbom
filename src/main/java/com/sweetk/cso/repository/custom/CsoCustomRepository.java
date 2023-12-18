package com.sweetk.cso.repository.custom;

import com.sweetk.cso.entity.Cso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CsoCustomRepository {

    List<Cso> findAllByCsoNm(String csoNm);

    Page<Cso> findPageAllByCsoNm(String csoNm, Pageable pageable);
}
