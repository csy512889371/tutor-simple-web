package com.rjsoft.uums.facade.dict.service.impl;

import com.rjsoft.common.model.search.Searchable;
import com.rjsoft.uums.core.dict.service.UmsDictionaryTypeService;
import com.rjsoft.uums.facade.dict.entity.UmsDictionaryType;
import com.rjsoft.uums.facade.dict.service.UmsDictionaryTypeFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/10/9.
 */
@Service("umsDictionaryTypeFacadeImpl")
public class UmsDictionaryTypeFacadeImpl implements UmsDictionaryTypeFacade {
    @Autowired
    private UmsDictionaryTypeService umsDictionaryTypeService;
    @Override
    public UmsDictionaryType create(UmsDictionaryType umsDictionaryType) {
        return umsDictionaryTypeService.saveUmsDictionaryType(umsDictionaryType);
    }

    @Override
    public UmsDictionaryType update(UmsDictionaryType umsDictionaryType) {
        return umsDictionaryTypeService.update(umsDictionaryType);
    }

    @Override
    public void delete(String... ids) {
        umsDictionaryTypeService.delete(ids);
    }

    @Override
    public UmsDictionaryType getUmsDictionaryTypeById(String id){
        return umsDictionaryTypeService.getOne(id);
    }

    @Override
    public UmsDictionaryType getUmsDictionaryTypeByCode(String code) {
        return umsDictionaryTypeService.getUmsDictionaryTypeByCode(code);
    }

    @Override
    public Page<UmsDictionaryType> listPage(Searchable searchable) {
        return umsDictionaryTypeService.findAll(searchable);
    }

    @Override
    public List<UmsDictionaryType> list(Searchable searchable) {
        return umsDictionaryTypeService.findAllWithNoPageNoSort(searchable);
    }
}
