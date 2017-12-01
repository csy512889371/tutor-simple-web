package com.rjsoft.uums.facade.log.service.impl;

import com.rjsoft.common.model.search.Searchable;
import com.rjsoft.uums.core.log.service.UmsLogService;
import com.rjsoft.uums.facade.log.entity.UmsLog;
import com.rjsoft.uums.facade.log.service.UmsLogFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */
@Service("umsLogFacadeImpl")
public class UmsLogFacadeImpl implements UmsLogFacade {
    @Autowired
    private UmsLogService umsLogService;
    @Override
    public UmsLog save(UmsLog umsLog) {
        return umsLogService.save(umsLog);
    }

    @Override
    public Page<UmsLog> listPage(Searchable searchable) {
        return umsLogService.findAll(searchable);
    }

    @Override
    public List<UmsLog> list(Searchable searchable) {
        return umsLogService.findAllWithNoPageNoSort(searchable);
    }
}
