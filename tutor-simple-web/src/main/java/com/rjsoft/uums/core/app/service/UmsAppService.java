package com.rjsoft.uums.core.app.service;

import com.rjsoft.common.service.BaseService;
import com.rjsoft.uums.core.app.repository.UmsAppRepository;
import com.rjsoft.uums.facade.app.entity.UmsApp;
import com.rjsoft.uums.facade.app.exception.AppSnExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UmsAppService extends BaseService<UmsApp, String> {
    @Autowired
    private UmsAppRepository umsAppRepository;

    /**
     * 保存应用系统信息
     */
    public UmsApp saveApp(UmsApp app){
        if(findAppBySn(app.getSn())!=null){
            throw new AppSnExistsException();
        }
        return save(app);
    }

    /**
     * 根据应用系统标识符查找应用系统
     * @param appSn
     * @return
     */
//	@Cacheable(value="uumsCache",key="'umsApp_'+#appSn")
    public UmsApp findAppBySn(String appSn){
        return umsAppRepository.findAppBySn(appSn);
    }


}
