package com.rjsoft.uums.core.org.repository;

import com.rjsoft.common.repository.CustomRepository;
import com.rjsoft.uums.facade.org.entity.UmsOrg;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UmsOrgRepository extends CustomRepository<UmsOrg, String> {

    @Query("select count(o.id) from UmsOrg o where o.typeId=?1 and o.id=?2")
    public Integer getOrgNumsByType(String typeId, String pId);

    @Query("select count(o.id) from UmsOrg o where o.typeId=?1")
    public Integer getOrgNumsByType(String orgTypeId);

    @Query("select o.orderNum from UmsOrg o where o.id=?1")
    public Integer getMaxOrder(String pid);

    @Query("select o from UmsOrg o where o.parent.id=?1")
    List<UmsOrg> findByParentId(String parentId);
}
