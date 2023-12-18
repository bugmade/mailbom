package com.sweetk.cso.repository;

import com.sweetk.cso.entity.Product;
import com.sweetk.cso.repository.custom.ProductCustomRepository;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long>, ProductCustomRepository {

    Product findById(long id);
}
