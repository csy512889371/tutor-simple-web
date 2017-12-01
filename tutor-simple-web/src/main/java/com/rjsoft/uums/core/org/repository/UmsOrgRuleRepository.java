package com.rjsoft.uums.core.org.repository;

import com.rjsoft.common.repository.CustomRepository;
import com.rjsoft.uums.facade.org.entity.UmsOrgRule;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UmsOrgRuleRepository extends CustomRepository<UmsOrgRule, String> {

    @Query("select o from UmsOrgRule o where o.orgId=?1")
    public UmsOrgRule loadManagerOrg(String orgId);

    @Modifying
    @Query("delete from UmsOrgRule o where o.orgId=?1")
    public void deleteOrgRuleByOrg(String orgId);

}
