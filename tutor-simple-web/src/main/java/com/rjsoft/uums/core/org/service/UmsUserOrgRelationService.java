package com.rjsoft.uums.core.org.service;

import com.rjsoft.common.service.BaseService;
import com.rjsoft.uums.core.org.repository.UmsUserOrgRelationRepository;
import com.rjsoft.uums.facade.org.entity.UmsUserOrgRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("umsUserOrgRelationService")
public class UmsUserOrgRelationService extends BaseService<UmsUserOrgRelation,String> {

    @Autowired
    private UmsUserOrgRelationRepository umsUserOrgRelationRepository;

    public void deleteByUserId(String userId){
        umsUserOrgRelationRepository.clearUserOrgRelationByUserId(userId);
    }
}