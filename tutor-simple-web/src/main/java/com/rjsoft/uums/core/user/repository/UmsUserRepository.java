package com.rjsoft.uums.core.user.repository;

import com.rjsoft.common.repository.CustomRepository;
import com.rjsoft.uums.facade.user.entity.UmsUser;

import java.util.List;

public interface UmsUserRepository extends CustomRepository<UmsUser, String> {

    UmsUser findByUsername(String username);

    List<UmsUser> findByNickname(String nickname);
}
