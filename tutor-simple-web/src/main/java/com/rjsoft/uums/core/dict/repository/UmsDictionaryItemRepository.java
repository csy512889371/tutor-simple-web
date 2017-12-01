package com.rjsoft.uums.core.dict.repository;

import com.rjsoft.common.repository.CustomRepository;
import com.rjsoft.uums.facade.dict.entity.UmsDictionaryItem;
import com.rjsoft.uums.facade.dict.entity.UmsDictionaryType;
import org.springframework.stereotype.Repository;

@Repository
public interface UmsDictionaryItemRepository extends CustomRepository<UmsDictionaryItem, String> {
    public UmsDictionaryItem findByCodeAndType(Integer code, UmsDictionaryType type);

}
