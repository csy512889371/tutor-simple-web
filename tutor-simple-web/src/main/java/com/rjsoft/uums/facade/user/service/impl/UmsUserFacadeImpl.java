package com.rjsoft.uums.facade.user.service.impl;

import com.rjsoft.common.model.search.Searchable;
import com.rjsoft.uums.core.department.service.UmsUserDepartmentRelationService;
import com.rjsoft.uums.core.org.service.UmsUserOrgRelationService;
import com.rjsoft.uums.core.orgRole.service.UmsUserOrgRoleRelationService;
import com.rjsoft.uums.core.position.service.UmsUserPositionRelationService;
import com.rjsoft.uums.core.role.service.UmsUserRoleRelationService;
import com.rjsoft.uums.core.user.service.UmsUserService;
import com.rjsoft.uums.facade.user.entity.UmsUser;
import com.rjsoft.uums.facade.user.exception.*;
import com.rjsoft.uums.facade.user.service.UmsUserFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service("umsUserFacadeImpl")
public class UmsUserFacadeImpl implements UmsUserFacade {
	
	private Logger logger = LoggerFactory.getLogger(UmsUserFacadeImpl.class);
	
	@Autowired
	private UmsUserService umsUserService;

	@Autowired
	private UmsUserOrgRelationService umsUserOrgRelationServicey;
	
	@Autowired
	private UmsUserDepartmentRelationService umsUserDepartmentRelationService;
	
	@Autowired
	private UmsUserPositionRelationService umsUserPositionRelationService;
	
	@Autowired
	private UmsUserOrgRoleRelationService umsUserOrgRoleRelationService;
	
	@Autowired
	private UmsUserRoleRelationService umsUserRoleRelationService;

	@Override
	public UmsUser login(String username, String password) throws UserNotExistsException,UserUnAvailableException,UserPasswordNotMatchException {
		return umsUserService.login(username, password);
	}

	@Override
	public UmsUser register(UmsUser user) throws UserUsernameEmptyException,UserPasswordEmptyException,UserNicknameEmptyException,UserUsernameNotValidException,UserPasswordNotValidException,UserNicknameNotValidException,UserUsernameExistsException {
		return umsUserService.register(user);
	}

	@Override
	public UmsUser resetPassword(String username, String newPassword) {
		return umsUserService.changePassword(findByUsername(username), newPassword);
	}

	@Override
	public UmsUser updatePassword(String username, String oldPassword, String newPassword) {
		UmsUser user = umsUserService.login(username, oldPassword);
		if(user == null){
			logger.info(username+"用户不存在");
			throw new UserNotExistsException();
		}
		user = resetPassword(username, newPassword);
		return user;
	}

	@Override
	public UmsUser findByUsername(String username) {
		return umsUserService.findByUsername(username);
	}

	@Override
	public Page<UmsUser> listPage(Searchable searchable) {
		return umsUserService.findAll(searchable);
	}

	@Override
	public UmsUser update(UmsUser user) {
		return umsUserService.update(user);
	}

	@Override
	public UmsUser updAvailable(String id, Short isAvailable) {
		UmsUser user = umsUserService.getOne(id);
		user.setIsAvailable(isAvailable);
		user = umsUserService.update(user);
		return user;
	}

	@Override
	public UmsUser findById(String id) {
		return umsUserService.getOne(id);
	}

	@Override
	public void delete(String... ids) {
		for(String id : ids){
			umsUserOrgRelationServicey.deleteByUserId(id);
			umsUserDepartmentRelationService.deleteByUserId(id);
			umsUserPositionRelationService.deleteByUserId(id);
			umsUserOrgRoleRelationService.deleteByUserId(id);
			umsUserRoleRelationService.deleteByUserId(id);
			UmsUser user = umsUserService.getOne(id);
			if(user != null) umsUserService.delete(user);
		}
	}
}
