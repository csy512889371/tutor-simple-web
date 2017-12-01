package com.rjsoft.uums.core.resource.repository;

import com.rjsoft.common.repository.CustomRepository;
import com.rjsoft.uums.facade.resource.entity.UmsButton;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UmsButtonRepository extends CustomRepository<UmsButton, String> {

    @Query("select a from UmsButton a,UmsMenuButton b where a.id=b.buttonId and b.menuId=?1 and a.isAvailable=?2")
    public List<UmsButton> findButtonsByMenuId(String menuId, Short isAvailable);
}
