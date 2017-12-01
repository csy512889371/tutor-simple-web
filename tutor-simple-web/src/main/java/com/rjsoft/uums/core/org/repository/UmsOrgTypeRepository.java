package com.rjsoft.uums.core.org.repository;

import com.rjsoft.common.repository.CustomRepository;
import com.rjsoft.uums.facade.org.entity.UmsOrgType;
import org.springframework.data.jpa.repository.Query;

public interface UmsOrgTypeRepository extends CustomRepository<UmsOrgType, String> {
    @Query("select o from UmsOrgType o where o.sn=?1")
    public UmsOrgType findOrgTypeBySn(String sn);
}
