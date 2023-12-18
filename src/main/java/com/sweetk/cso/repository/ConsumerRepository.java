package com.sweetk.cso.repository;

import com.sweetk.cso.entity.Consumer;
import com.sweetk.cso.entity.Stock;
import com.sweetk.cso.repository.custom.ConsumerCustomRepository;
import com.sweetk.cso.repository.custom.StockCustomRepository;
import org.springframework.data.repository.CrudRepository;

public interface ConsumerRepository extends CrudRepository<Consumer, Long>, ConsumerCustomRepository {

    Consumer findById(long id);
}
