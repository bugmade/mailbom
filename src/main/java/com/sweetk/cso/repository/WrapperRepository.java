package com.sweetk.cso.repository;

import com.sweetk.cso.entity.Product;
import com.sweetk.cso.entity.Wrapper;
import com.sweetk.cso.repository.custom.ProductCustomRepository;
import com.sweetk.cso.repository.custom.WrapperCustomRepository;
import org.springframework.data.repository.CrudRepository;

public interface WrapperRepository extends CrudRepository<Wrapper, Long>, WrapperCustomRepository {

    Wrapper findById(long id);
}
