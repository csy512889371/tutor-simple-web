package com.rjsoft.uums.core.role.service;

import com.rjsoft.common.service.BaseService;
import com.rjsoft.uums.core.role.repository.UmsUserRoleRelationRepository;
import com.rjsoft.uums.facade.role.entity.UmsUserRoleRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("umsUserRoleRelationService")
public class UmsUserRoleRelationService extends BaseService<UmsUserRoleRelation, String> {

    @Autowired
    private UmsUserRoleRelationRepository umsUserRoleRelationRepository;

    public void deleteByAppIds(List<String> appIds) {
        umsUserRoleRelationRepository.deleteByAppIds(appIds);
    }

    public void deleteByRoleIds(List<String> roleIds) {
        umsUserRoleRelationRepository.deleteByRoleIds(roleIds);
    }

    public void deleteByUserId(String userId) {
        umsUserRoleRelationRepository.clearUserRoleRelationByUserId(userId);
    }
}