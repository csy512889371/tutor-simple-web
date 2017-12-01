package com.rjsoft.uums.core.orgRole.repository;

import com.rjsoft.common.repository.CustomRepository;
import com.rjsoft.uums.facade.orgRole.entity.UmsOrgRole;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UmsOrgRoleRepository extends CustomRepository<UmsOrgRole, String> {
    @Query("select o from UmsOrgRole o where o.sn=?1")
    public UmsOrgRole findByRoleSn(String roleSn);

    public void deleteByOrgIdIn(String[] orgIds);

    List<UmsOrgRole> findByOrgId(String orgId);
}
