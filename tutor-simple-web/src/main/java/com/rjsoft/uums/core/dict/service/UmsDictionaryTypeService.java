package com.rjsoft.uums.core.dict.service;

import com.rjsoft.common.service.BaseService;
import com.rjsoft.uums.core.dict.repository.UmsDictionaryTypeRepository;
import com.rjsoft.uums.facade.dict.entity.UmsDictionaryType;
import com.rjsoft.uums.facade.dict.exception.DictionaryTypeCodeExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UmsDictionaryTypeService extends BaseService<UmsDictionaryType, String> {
    @Autowired
    private UmsDictionaryTypeRepository umsDictionaryTypeRepository;

    public UmsDictionaryType saveUmsDictionaryType(UmsDictionaryType type) {
        if (getUmsDictionaryTypeByCode(type.getCode()) != null) {
            throw new DictionaryTypeCodeExistsException();
        }
        return save(type);
    }

    public UmsDictionaryType getUmsDictionaryTypeByCode(String code) {
        return umsDictionaryTypeRepository.findByCode(code);
    }
}
