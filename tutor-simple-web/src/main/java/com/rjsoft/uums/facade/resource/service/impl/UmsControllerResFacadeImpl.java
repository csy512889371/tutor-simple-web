package com.rjsoft.uums.facade.resource.service.impl;

import com.rjsoft.common.entity.enums.AvailableEnum;
import com.rjsoft.common.model.search.Searchable;
import com.rjsoft.uums.core.app.service.UmsAppService;
import com.rjsoft.uums.core.resource.service.UmsControllerOperService;
import com.rjsoft.uums.core.resource.service.UmsControllerResService;
import com.rjsoft.uums.core.resource.service.UmsMenuResService;
import com.rjsoft.uums.facade.app.entity.UmsApp;
import com.rjsoft.uums.facade.resource.entity.UmsControllerOper;
import com.rjsoft.uums.facade.resource.entity.UmsControllerResources;
import com.rjsoft.uums.facade.resource.entity.UmsMenuResources;
import com.rjsoft.uums.facade.resource.exception.ControllerSnExistsException;
import com.rjsoft.uums.facade.resource.exception.ResourceException;
import com.rjsoft.uums.facade.resource.service.UmsControllerResFacade;
import com.rjsoft.uums.facade.user.entity.UmsUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service("umsControllerResFacadeImpl")
public class UmsControllerResFacadeImpl implements UmsControllerResFacade {
    @Autowired
    private UmsControllerResService umsControllerResService;
    @Autowired
    private UmsControllerOperService umsControllerOperService;
    @Autowired
    private UmsAppService umsAppService;
    @Autowired
    private UmsMenuResService umsMenuResService;

    public UmsControllerResources create(UmsControllerResources umsControllerResources) throws ControllerSnExistsException {
        UmsApp app = umsControllerResources.getApplication();
        String appId = null;
        if (app != null) appId = app.getId();
        UmsMenuResources menu = umsControllerResources.getMenu();
        String menuId = null;
        if (menu != null) menuId = menu.getId();
        if (appId != null) {
            UmsApp _app = umsAppService.getOne(appId);
            if (_app == null) {
                throw new ResourceException("controller.app.not.exited", null);
            }
        }
        if (menuId == null) {
            if (appId == null) {
                throw new ResourceException("controller.app.is.null", null);
            }
        } else {
            UmsMenuResources _menu = umsMenuResService.getOne(menuId);
            if (_menu == null) {
                throw new ResourceException("controller.menu.not.exited", null);
            } else {
                if (appId == null) {
                    UmsApp menuApp = _menu.getApplication();
                    if (menuApp != null) {
                        appId = menuApp.getId();
                        app = new UmsApp();
                        app.setId(appId);
                        umsControllerResources.setApplication(app);
                    }
                }
            }
        }
        return umsControllerResService.save(umsControllerResources);
    }

    @Override
    public UmsControllerResources update(UmsControllerResources umsControllerResources) {
        return umsControllerResService.update(umsControllerResources);
    }

    @Override
    public void deleteControllerRes(String... ids) {
        umsControllerResService.delete(ids);
    }

    @Override
    public Set<UmsControllerResources> getControllerResByUserAndAppSn(UmsUser user, String appSn, String menuId, Short isAvaiable) {
        return umsControllerResService.getControllerResByUserAndAppSn(user, appSn, menuId, isAvaiable);
    }

    @Override
    public List<UmsControllerResources> listUmsControllerRes(Searchable searchable) {
        return umsControllerResService.findAllWithSort(searchable);
    }

    @Override
    public Page<UmsControllerResources> listPageUmsControllerRes(Searchable searchable) {
        return umsControllerResService.findAll(searchable);
    }

    @Override
    public UmsControllerOper create(UmsControllerOper umsControllerOper) {
        return umsControllerOperService.save(umsControllerOper);
    }

    @Override
    public UmsControllerOper update(UmsControllerOper umsControllerOper) {
        return umsControllerOperService.update(umsControllerOper);
    }

    @Override
    public void deleteUmsControllerOper(String... ids) {
        umsControllerOperService.deleteControllerOper(ids);
    }

    @Override
    public List<UmsControllerOper> listUmsControllerOper(Searchable searchable) {
        return umsControllerOperService.findAllWithNoPageNoSort(searchable);
    }

    @Override
    public Page<UmsControllerOper> listPageUmsControllerOper(Searchable searchable) {
        return umsControllerOperService.findAll(searchable);
    }

    public UmsControllerResources updAvailable(String id, Short isAvailable) {
        UmsControllerResources cr = umsControllerResService.getOne(id);
        cr.setIsAvailable(isAvailable);
        cr = umsControllerResService.update(cr);
        return cr;
    }

    public UmsControllerResources findById(String id) {
        return umsControllerResService.getOne(id);
    }

    @Override
    public boolean containsUrlMappingForAuth(String url, UmsUser user, String appSn) {
        Set<UmsControllerResources> umsControllerRes = umsControllerResService.getControllerResByUrlAndUserAndAppSn(url, user, appSn, AvailableEnum.TRUE.getValue());
        if (umsControllerRes != null && umsControllerRes.size() > 0) {
            return true;
        }
        return false;
    }
}
