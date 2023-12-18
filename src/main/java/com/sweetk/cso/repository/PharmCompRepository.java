package com.sweetk.cso.repository;

import com.sweetk.cso.entity.Cso;
import com.sweetk.cso.entity.PharmComp;
import com.sweetk.cso.repository.custom.CsoCustomRepository;
import com.sweetk.cso.repository.custom.PharmCompCustomRepository;
import org.springframework.data.repository.CrudRepository;

public interface PharmCompRepository extends CrudRepository<PharmComp, Long>, PharmCompCustomRepository {
}
