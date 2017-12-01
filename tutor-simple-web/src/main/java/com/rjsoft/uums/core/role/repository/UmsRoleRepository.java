package com.rjsoft.uums.core.role.repository;

import com.rjsoft.common.repository.CustomRepository;
import com.rjsoft.uums.facade.role.entity.UmsRole;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UmsRoleRepository extends CustomRepository<UmsRole, String> {

    @Query("select o from UmsRole o where o.sn=?1")
    public UmsRole findByRoleSn(String roleSn);

    public void deleteByAppIdIn(List<String> appIds);
}
