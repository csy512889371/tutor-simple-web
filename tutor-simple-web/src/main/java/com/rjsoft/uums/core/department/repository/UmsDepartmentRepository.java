package com.rjsoft.uums.core.department.repository;

import com.rjsoft.common.repository.CustomRepository;
import com.rjsoft.uums.facade.department.entity.UmsDepartment;

import java.util.List;

public interface UmsDepartmentRepository extends CustomRepository<UmsDepartment,String>{
    List<UmsDepartment> findByOrgId(String orgId);
}
