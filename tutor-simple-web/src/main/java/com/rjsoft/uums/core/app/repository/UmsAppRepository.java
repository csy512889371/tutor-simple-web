package com.rjsoft.uums.core.app.repository;

import com.rjsoft.common.repository.CustomRepository;
import com.rjsoft.uums.facade.app.entity.UmsApp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UmsAppRepository extends CustomRepository<UmsApp,String> {

    @Query("select o from UmsApp o where o.sn=?1")
    public UmsApp findAppBySn(String appSn);

    @Query("select o from UmsApp o where o.name=?1")
    public UmsApp findAppByName(String name);


}
