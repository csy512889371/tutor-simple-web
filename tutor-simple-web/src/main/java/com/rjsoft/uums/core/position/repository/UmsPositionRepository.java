package com.rjsoft.uums.core.position.repository;

import com.rjsoft.common.repository.CustomRepository;
import com.rjsoft.uums.facade.position.entity.UmsPosition;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UmsPositionRepository extends CustomRepository<UmsPosition, String> {

    UmsPosition findBySn(String sn);

    List<UmsPosition> findByDepartmentId(String departmentId);

    List<UmsPosition> findByOrgId(String orgId);

    void deleteByDepartmentId(String departmentId);

    void deleteByOrgId(String orgId);
}
