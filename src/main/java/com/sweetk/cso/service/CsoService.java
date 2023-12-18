package com.sweetk.cso.service;

import com.sweetk.cso.entity.Cso;
import com.sweetk.cso.repository.CsoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class CsoService {

    final CsoRepository csoRepository;

    public void test(Pageable pageable){
        Cso cso = csoRepository.findById(1);
        log.info(cso.getCsoNm());

        List<Cso> list = csoRepository.findAllByCsoNm("CSO");
        log.info("----- All List -----");
        list.forEach(log::info);

        log.info("----- Page List 5개씩 -----");
        Page<Cso> pageList = csoRepository.findPageAllByCsoNm("CSO", pageable);
        log.info("total Count : {}", pageList.getTotalElements());
        log.info("total Page : {}", pageList.getTotalPages());
        log.info("current Page : {}", pageable.getPageNumber());
        log.info("current Offset : {}", pageable.getOffset());
        pageList.getContent().forEach(log::info);
    }

}
