package com.sweetk.cso.repository;

import com.sweetk.cso.entity.Adm;
import com.sweetk.cso.repository.custom.AdmCustomRepository;
import com.sweetk.cso.repository.custom.StockCustomRepository;
import org.springframework.data.repository.CrudRepository;

public interface AdmRepository extends CrudRepository<Adm, Long>, AdmCustomRepository {

    Adm findByAdmId(String admId);

}
