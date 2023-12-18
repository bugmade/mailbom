package com.sweetk.cso.repository.custom;

import com.sweetk.cso.dto.StockListRes;
import com.sweetk.cso.entity.Consumer;
import com.sweetk.cso.entity.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ConsumerCustomRepository {

    List<Consumer> findAllConsumer();

//    Page<Consumer> findPageAllByProNm(String proNm, Pageable pageable);
//
    Consumer findConsumerByCsmCd(String csmCd);

    String createConsumer(Map<String, Object> params);

    String updateConsumer(Map<String, Object> params);

    String deleteConsumerByCsmCd(String csmCd);
}
