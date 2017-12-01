package com.rjsoft.uums.api.service.role.impl;

import com.rjsoft.common.entity.enums.AvailableEnum;
import com.rjsoft.common.model.search.Searchable;
import com.rjsoft.uums.api.service.role.RoleService;
import com.rjsoft.uums.facade.role.entity.UmsRole;
import com.rjsoft.uums.facade.role.entity.UmsUserRoleRelation;
import com.rjsoft.uums.facade.role.service.UmsRoleFacade;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Resource(name = "umsRoleFacadeImpl")
    private UmsRoleFacade umsRoleFacade;

    @Override
    public boolean validate(String username, String sn) {
        if (username == null || sn == null) {
            return false;
        }
        Searchable searchable = Searchable.newSearchable();
        searchable.addSearchParam("user.username_eq", username);
        searchable.addSearchParam("user.isAvailable_eq", AvailableEnum.TRUE.getValue());
        searchable.addSearchParam("role.isAvailable_eq", AvailableEnum.TRUE.getValue());
        List<UmsUserRoleRelation> uurrs = umsRoleFacade.listUmsUserRoleRelation(searchable);
        for (int i = 0; i < uurrs.size(); i++) {
            UmsUserRoleRelation uurr = uurrs.get(i);
            UmsRole role = uurr.getRole();
            if (role != null && sn.equals(role.getSn())) return true;
        }
        return false;
    }
}
