package com.rjsoft.uums.core.resource.repository;

import com.rjsoft.common.repository.CustomRepository;
import com.rjsoft.uums.facade.resource.entity.UmsControllerOper;

public interface UmsControllerOperRepository extends CustomRepository<UmsControllerOper, String> {
    public UmsControllerOper findByControllerOperSn(String controllerOperSn);
}
