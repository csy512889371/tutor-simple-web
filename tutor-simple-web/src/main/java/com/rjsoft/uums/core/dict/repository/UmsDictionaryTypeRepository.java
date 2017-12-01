package com.rjsoft.uums.core.dict.repository;

import com.rjsoft.common.repository.CustomRepository;
import com.rjsoft.common.repository.CustomRepositoryImpl;
import com.rjsoft.uums.facade.dict.entity.UmsDictionaryType;
import org.springframework.stereotype.Repository;

@Repository
public interface UmsDictionaryTypeRepository extends CustomRepository<UmsDictionaryType, String> {

    public UmsDictionaryType findByCode(String code);

}
