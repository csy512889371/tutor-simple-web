package com.rjsoft.uums.core.app.service;

import com.rjsoft.common.service.BaseService;
import com.rjsoft.uums.core.app.repository.UmsAppRepository;
import com.rjsoft.uums.facade.app.entity.UmsApp;
import com.rjsoft.uums.facade.app.exception.AppSnExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UmsAppService extends BaseService<UmsApp, String> {

    @Autowired
    private UmsAppRepository umsAppRepository;
    /**
     * 保存应用系统信息
     * @param app
     */
//	@CachePut(value="uumsCache",key="'umsApp_'+#app.sn")
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

    /**
     * 根据系统名称查询系统实体信息
     * @param name
     * @return
     */
    public UmsApp findAppByName(String name){
        return umsAppRepository.findAppByName(name);
    }

    public List<UmsApp> findAppByUserRoleRelation(String userId, Short isAvailable){
        return umsAppRepository.findAppByUserRoleRelation(userId, isAvailable);
    }

    public List<UmsApp> findAppByUserRoleRelation(String userId, String roleSn){
        return umsAppRepository.findAppByUserRoleRelation(userId, roleSn);
    }

    public List<UmsApp> findAppByUserRoleRelation(String userId, Short isAvailable, String roleSn){
        return umsAppRepository.findAppByUserRoleRelation(userId, isAvailable, roleSn);
    }

    public List<UmsApp> list(String name, String userId, String roleSn){
        return umsAppRepository.findAppByUserRoleRelation(name, userId, roleSn);
    }

    public List<UmsApp> list(String name, String userId, Short isAvailable, String roleSn){
        return umsAppRepository.findAppByUserRoleRelation(name, userId, isAvailable, roleSn);
    }

    public Page<UmsApp> listPage(String name, String userId, Pageable page){
        return umsAppRepository.findAppByUserRoleRelation(name, userId, page);
    }

    public Page<UmsApp> listPage(String name, String userId, String roleSn, Pageable page){
        return umsAppRepository.findAppByUserRoleRelation(name, userId, roleSn, page);
    }

    public Page<UmsApp> listPage(String name, String userId, Short isAvailable, Pageable page){
        return umsAppRepository.findAppByUserRoleRelation(name, userId, isAvailable, page);
    }

    public Page<UmsApp> listPage(String name, String userId, Short isAvailable, String roleSn, Pageable page){
        return umsAppRepository.findAppByUserRoleRelation(name, userId, isAvailable, roleSn, page);
    }
}
