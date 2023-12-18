package com.sweetk.cso.repository;

import com.sweetk.cso.entity.Product;
import com.sweetk.cso.entity.Stock;
import com.sweetk.cso.repository.custom.ProductCustomRepository;
import com.sweetk.cso.repository.custom.StockCustomRepository;
import org.springframework.data.repository.CrudRepository;

public interface StockRepository extends CrudRepository<Stock, Long>, StockCustomRepository {

    Stock findById(long id);
}
