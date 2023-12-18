package com.sweetk.cso.repository;

import com.sweetk.cso.entity.Cso;
import com.sweetk.cso.repository.custom.CsoCustomRepository;
import org.springframework.data.repository.CrudRepository;

public interface CsoRepository extends CrudRepository<Cso, Long>, CsoCustomRepository {

    Cso findById(long id);

}
