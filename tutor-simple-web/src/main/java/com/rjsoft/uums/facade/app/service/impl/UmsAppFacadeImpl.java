package com.rjsoft.uums.facade.app.service.impl;

import com.rjsoft.uums.core.app.service.UmsAppService;
import com.rjsoft.uums.facade.app.entity.UmsApp;
import com.rjsoft.uums.facade.app.exception.AppSnExistsException;
import com.rjsoft.uums.facade.app.service.UmsAppFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("umsAppFacadeImpl")
public class UmsAppFacadeImpl implements UmsAppFacade {

    @Autowired
    private UmsAppService umsAppService;

    @Override
    public UmsApp register(UmsApp app) throws AppSnExistsException {
        return umsAppService.saveApp(app);
    }


}
