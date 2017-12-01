package com.rjsoft.uums.core.resource.repository;

import com.rjsoft.common.repository.CustomRepository;
import com.rjsoft.uums.facade.resource.entity.UmsMenuButton;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UmsMenuButtonRepository extends CustomRepository<UmsMenuButton, String> {

    @Query("select o.menuId from UmsMenuButton o where o.buttonId=?1")
    public List<String> findMenuIdByButtonId(String buttonId);

    @Modifying
    @Query("delete from UmsMenuButton o where o.menuId=?1")
    public void deleteMenuButtonByMenuId(String menuId);

    @Modifying
    @Query("delete from UmsMenuButton o where o.buttonId=?1")
    public void deleteMenuButtonByButtonId(String menuId);

}
