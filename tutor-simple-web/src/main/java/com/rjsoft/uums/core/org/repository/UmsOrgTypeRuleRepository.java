package com.rjsoft.uums.core.org.repository;

import com.rjsoft.common.repository.CustomRepository;
import com.rjsoft.uums.facade.org.entity.UmsOrgTypeRule;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UmsOrgTypeRuleRepository extends CustomRepository<UmsOrgTypeRule, String> {

    @Query("select o.num from UmsOrgTypeRule o where o.pid=?1 and o.cid=?2")
    public Integer loadOrgTypeRuleNum(String pTypeId, String cTypeId);

    @Query("select o from UmsOrgTypeRule o where o.pid=?1 and o.cid=?2")
    public List<UmsOrgTypeRule> listByRule(String pid);

    @Modifying
    @Query("delete from UmsOrgTypeRule where pid=?1")
    public void deleteOrgTypeRuleByOrgType(String pTypeId);

}
