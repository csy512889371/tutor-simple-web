package com.rjsoft.uums.core.dict.service;

import com.rjsoft.common.service.BaseService;
import com.rjsoft.uums.core.dict.repository.UmsDictionaryItemRepository;
import com.rjsoft.uums.facade.dict.entity.UmsDictionaryItem;
import com.rjsoft.uums.facade.dict.entity.UmsDictionaryType;
import com.rjsoft.uums.facade.dict.exception.DictionaryItemCodeExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UmsDictionaryItemService extends BaseService<UmsDictionaryItem, String> {

    @Autowired
    private UmsDictionaryItemRepository umsDictionaryItemRepository;

    public UmsDictionaryItem saveUmsDictionaryItem(UmsDictionaryItem item) {
        if (getUmsDictionaryTypeByCode(item.getCode(), item.getType()) != null) {
            throw new DictionaryItemCodeExistsException();
        }
        return save(item);
    }

    public UmsDictionaryItem getUmsDictionaryTypeByCode(Integer code, UmsDictionaryType type) {
        return umsDictionaryItemRepository.findByCodeAndType(code, type);
    }

}
