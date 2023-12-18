package com.sweetk.cso.service;

import com.sweetk.cso.dto.pharmComp.PharmCompListReq;
import com.sweetk.cso.dto.pharmComp.PharmCompListRes;
import com.sweetk.cso.repository.PharmCompRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PharmCompService {

    private final PharmCompRepository pharmCompRepository;

    @Transactional(readOnly = true)
    public Page<PharmCompListRes> getList(PharmCompListReq req, Pageable pageable) {
        // TODO COM_CM 이름
        return pharmCompRepository.getListBySearchDtoAndPageable(req, pageable);
    }

}
