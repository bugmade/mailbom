package com.sweetk.cso.repository;

import com.sweetk.cso.entity.WprIo;
import com.sweetk.cso.entity.Wrapper;
import com.sweetk.cso.repository.custom.WprIoCustomRepository;
import com.sweetk.cso.repository.custom.WrapperCustomRepository;
import org.springframework.data.repository.CrudRepository;

public interface WprIoRepository extends CrudRepository<WprIo, Long>, WprIoCustomRepository {

    WprIo findById(long id);
}
