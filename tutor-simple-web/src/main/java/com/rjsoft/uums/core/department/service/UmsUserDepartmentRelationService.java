package com.rjsoft.uums.core.department.service;

import com.rjsoft.common.service.BaseService;
import com.rjsoft.uums.core.department.repository.UmsUserDepartmentRelationRepository;
import com.rjsoft.uums.facade.department.entity.UmsUserDepartmentRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UmsUserDepartmentRelationService extends BaseService<UmsUserDepartmentRelation,String> {

    @Autowired
    private UmsUserDepartmentRelationRepository umsUserDepartmentRelationRepository;

    public void deleteByUserId(String userId){
        umsUserDepartmentRelationRepository.clearUserDepartmentRelationByUserId(userId);
    }
}
