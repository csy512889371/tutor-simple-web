package com.rjsoft.uums.core.orgRole.service;

import com.rjsoft.common.service.BaseService;
import com.rjsoft.uums.core.orgRole.repository.UmsUserOrgRoleRelationRepository;
import com.rjsoft.uums.facade.orgRole.entity.UmsUserOrgRoleRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("umsUserOrgRoleRelationService")
public class UmsUserOrgRoleRelationService extends BaseService<UmsUserOrgRoleRelation,String> {

    @Autowired
    private UmsUserOrgRoleRelationRepository umsUserOrgRoleRelationRepository;

    public void deleteByUserId(String userId){
        umsUserOrgRoleRelationRepository.clearUserRoleRelationByUserId(userId);
    }
}