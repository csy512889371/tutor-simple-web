package com.rjsoft.uums.core.user.repository;

import com.rjsoft.common.repository.CustomRepository;
import com.rjsoft.uums.facade.user.entity.UmsPerson;

import java.util.List;

public interface UmsPersonRepository extends CustomRepository<UmsPerson, String> {

    List<UmsPerson> findBySn(String sn);

    List<UmsPerson> findByName(String name);
}
