package com.rjsoft.uums.facade.dict.service.impl;

import com.rjsoft.common.model.search.Searchable;
import com.rjsoft.uums.core.dict.service.UmsDictionaryItemService;
import com.rjsoft.uums.facade.dict.entity.UmsDictionaryItem;
import com.rjsoft.uums.facade.dict.entity.UmsDictionaryType;
import com.rjsoft.uums.facade.dict.service.UmsDictionaryItemFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/10/9.
 */
@Service("umsDictionaryItemFacadeImpl")
public class UmsDictionaryItemFacadeImpl implements UmsDictionaryItemFacade {
    @Autowired
    private UmsDictionaryItemService umsDictionaryItemService;

    @Override
    public UmsDictionaryItem create(UmsDictionaryItem umsDictionaryItem) {
        return umsDictionaryItemService.saveUmsDictionaryItem(umsDictionaryItem);
    }

    @Override
    public UmsDictionaryItem update(UmsDictionaryItem umsDictionaryItem) {
        return umsDictionaryItemService.update(umsDictionaryItem);
    }

    @Override
    public void delete(String... ids) {
        umsDictionaryItemService.delete(ids);
    }

    @Override
    public UmsDictionaryItem getUmsDictionaryItemById(String id) {
        return umsDictionaryItemService.getOne(id);
    }

    @Override
    public UmsDictionaryItem getUmsDictionaryItemByCodeAndType(Integer code, UmsDictionaryType type) {
        return umsDictionaryItemService.getUmsDictionaryTypeByCode(code, type);
    }

    @Override
    public Page<UmsDictionaryItem> listPage(Searchable searchable) {
        return umsDictionaryItemService.findAll(searchable);
    }

    @Override
    public List<UmsDictionaryItem> list(Searchable searchable) {
        return umsDictionaryItemService.findAllWithNoPageNoSort(searchable);
    }
}
