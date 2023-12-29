package com.sweetk.cso.repository;

import com.sweetk.cso.entity.Sales;
import com.sweetk.cso.entity.Stock;
import com.sweetk.cso.repository.custom.SalesCustomRepository;
import com.sweetk.cso.repository.custom.StockCustomRepository;
import org.springframework.data.repository.CrudRepository;

public interface SalesRepository extends CrudRepository<Sales, Long>, SalesCustomRepository {

    Sales findById(long id);
}
