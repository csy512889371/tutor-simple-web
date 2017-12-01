package com.rjsoft.uums.api.service.orgRole.impl;

import com.rjsoft.common.entity.enums.AvailableEnum;
import com.rjsoft.common.model.search.Searchable;
import com.rjsoft.uums.api.constant.FayOrgRoleConstant;
import com.rjsoft.uums.api.service.orgRole.OrgRoleService;
import com.rjsoft.uums.facade.orgRole.entity.UmsOrgRole;
import com.rjsoft.uums.facade.orgRole.entity.UmsUserOrgRoleRelation;
import com.rjsoft.uums.facade.orgRole.service.UmsOrgRoleFacade;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OrgRoleServiceImpl implements OrgRoleService {

	@Resource
	private UmsOrgRoleFacade umsOrgRoleFacade;
	
	@Override
	public boolean validate(String username, String sn) {
		if(username == null || sn == null){
			return false;
		}
		Searchable searchable = Searchable.newSearchable();
		searchable.addSearchParam("user.username_eq", username);
		searchable.addSearchParam("user.isAvailable_eq", AvailableEnum.TRUE.getValue());
		searchable.addSearchParam("role.isAvailable_eq", AvailableEnum.TRUE.getValue());
		List<UmsUserOrgRoleRelation> uuorrs = umsOrgRoleFacade.listUmsUserRoleRelation(searchable);
		for(int i = 0; i < uuorrs.size(); i++){
			UmsUserOrgRoleRelation uuorr = uuorrs.get(i);
			UmsOrgRole role = uuorr.getRole();
			if(role != null && sn.startsWith(FayOrgRoleConstant.ORG_SUPER_MANAGE_ROLE_SN_PREFIX)) return true;
		}
		return false;
	}

}
