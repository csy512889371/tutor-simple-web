package com.rjsoft.uums.api.service.appResource.impl;

import com.rjsoft.common.entity.enums.AvailableEnum;
import com.rjsoft.common.model.search.Searchable;
import com.rjsoft.common.vo.ListVO;
import com.rjsoft.uums.api.constant.FayOrgRoleConstant;
import com.rjsoft.uums.api.constant.FaySysRoleConstant;
import com.rjsoft.uums.api.constant.FayUserConstant;
import com.rjsoft.uums.api.controller.vo.unify.ResourceTreeVO;
import com.rjsoft.uums.api.service.appResource.AppResourceService;
import com.rjsoft.uums.api.service.orgRole.OrgRoleService;
import com.rjsoft.uums.api.service.role.RoleService;
import com.rjsoft.uums.api.util.tree.FayTreeUtil;
import com.rjsoft.uums.facade.app.entity.UmsApp;
import com.rjsoft.uums.facade.app.service.UmsAppFacade;
import com.rjsoft.uums.facade.resource.entity.UmsControllerResources;
import com.rjsoft.uums.facade.resource.entity.UmsMenuResources;
import com.rjsoft.uums.facade.resource.service.UmsControllerResFacade;
import com.rjsoft.uums.facade.resource.service.UmsMenuResFacade;
import com.rjsoft.uums.facade.user.entity.UmsUser;
import com.rjsoft.uums.facade.user.service.UmsUserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
public class AppResourceServiceImpl implements AppResourceService {
	
	@Resource
	private UmsAppFacade umsAppFacade;

	@Resource
	private UmsUserFacade umsUserFacade;

	@Resource
	private UmsMenuResFacade umsMenuResFacade;

	@Resource
	private UmsControllerResFacade umsControllerResFacade;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private OrgRoleService orgRoleService;

	@Override
	public Object findAppResourceInTreeByLoginUser(String name, String currentUsername,
			List<String> currentUserRoleSn) {
		ListVO<ResourceTreeVO> listVO = null;
		if(Arrays.asList(FayUserConstant.SUPER_MANAGE_USERNAME).contains(currentUsername) || roleService.validate(currentUsername, FaySysRoleConstant.SUPER_MANAGE_ROLE_SN)){
			listVO = getAppResource(name);
			for(ResourceTreeVO rto : listVO.getVoList()){
				rto.setAvailable("1");
			}
		}else if(orgRoleService.validate(currentUsername, FayOrgRoleConstant.ORG_SUPER_MANAGE_ROLE_SN)){
			listVO = getAppResourceByOrgs(name, currentUsername);
		}else{
			listVO = getDefaultAppResource(name, currentUsername);
		}
		Object data = FayTreeUtil.getTreeInJsonObject(listVO.getVoList());
		return data;
	}
	
	private ListVO<ResourceTreeVO> getAppResource(String name){
		ListVO<ResourceTreeVO> listVO = getFromUumsApp(name);
		listVO.getVoList().addAll(getFromUumsMenu(name, null).getVoList());
		listVO.getVoList().addAll(getFromUumsController(name, null).getVoList());
		return listVO;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private ListVO<ResourceTreeVO> getAppResourceByOrgs(String name, String currentUsername){
		ListVO<ResourceTreeVO> listVO = new ListVO<>(new ArrayList<>(), ResourceTreeVO.class);
		
		UmsUser user = umsUserFacade.findByUsername(currentUsername);
		
		Searchable appSearchable = Searchable.newSearchable();
		List<UmsApp> appList = umsAppFacade.list(appSearchable);
		
		List<UmsApp> needApps = new ArrayList<>();
		
		for(UmsApp app : appList){
			String appSn = app.getSn();
			Set<UmsMenuResources> menuSet= umsMenuResFacade.getMenusByUserAndAppSn(user, appSn, AvailableEnum.TRUE.getValue());
			Set<UmsControllerResources> controllerSet = umsControllerResFacade.getControllerResByUserAndAppSn(user, appSn, null, AvailableEnum.TRUE.getValue());
			if(!menuSet.isEmpty()){
				listVO.getVoList().addAll(new ListVO(menuSet, ResourceTreeVO.class).getVoList());
			}
			if(!controllerSet.isEmpty()){
				listVO.getVoList().addAll(new ListVO(controllerSet, ResourceTreeVO.class).getVoList());
			}
			if(!(menuSet.isEmpty() && controllerSet.isEmpty())){
				needApps.add(app);
			}
		}
		listVO.getVoList().addAll(new ListVO(needApps, ResourceTreeVO.class).getVoList());
		return listVO;
	}
	
	private ListVO<ResourceTreeVO> getDefaultAppResource(String name, String currentUsername){
		ListVO<ResourceTreeVO> listVO = getFromUumsUserApp(name, currentUsername);
		listVO.getVoList().addAll(getFromUumsUserMenu(name, null, currentUsername).getVoList());
		listVO.getVoList().addAll(getFromUumsUserController(name, null, currentUsername).getVoList());
		return listVO;
	}
	
	private ListVO<ResourceTreeVO> getFromUumsApp(String name){
		Searchable searchable = Searchable.newSearchable();
		searchable.addSearchParam("name_like", name);
		List<UmsApp> apps = umsAppFacade.list(searchable);
		ListVO<ResourceTreeVO> listVO = new ListVO<>(apps, ResourceTreeVO.class);
		return listVO;
	}
	
	private ListVO<ResourceTreeVO> getFromUumsUserApp(String name, String username){
		Searchable searchable = Searchable.newSearchable();
		searchable.addSearchParam("name_like", name);
		UmsUser user = umsUserFacade.findByUsername(username);
		List<UmsApp> apps = umsAppFacade.list(name, user.getId(), FaySysRoleConstant.MANAGE_ROLE_SN_PREFIX);
		ListVO<ResourceTreeVO> listVO = new ListVO<>(apps, ResourceTreeVO.class);
		return listVO;
	}
	
	private ListVO<ResourceTreeVO> getFromUumsMenu(String name, String appId){
		Searchable searchable = Searchable.newSearchable();
		searchable.addSearchParam("application.id_eq", appId);
		searchable.addSort(Direction.ASC, "menuOrder");
		searchable.addSearchParam("menuName_like", name);
		List<UmsMenuResources> list = umsMenuResFacade.listUmsMenuResources(searchable);
		ListVO<ResourceTreeVO> listVO = new ListVO<>(list, ResourceTreeVO.class);
		return listVO;
	}
	
	private ListVO<ResourceTreeVO> getFromUumsController(String name, String appId){
		Searchable searchable = Searchable.newSearchable();
		searchable.addSearchParam("application.id_eq", appId);
		searchable.addSort(Direction.ASC, "controllerOrder");
		searchable.addSearchParam("controllerName_like", name);
		List<UmsControllerResources> list = umsControllerResFacade.listUmsControllerRes(searchable);
		ListVO<ResourceTreeVO> listVO = new ListVO<>(list, ResourceTreeVO.class);
		return listVO;
	}
	
	private ListVO<ResourceTreeVO> getFromUumsUserMenu(String name, String appId, String username){
		Searchable searchable = Searchable.newSearchable();
		
		UmsUser user = umsUserFacade.findByUsername(username);
		List<UmsApp> apps = umsAppFacade.findAppByUserRoleRelation(user.getId(), FaySysRoleConstant.MANAGE_ROLE_SN_PREFIX);
		List<String> appIds = new ArrayList<>();
		boolean flag = true;
		for(UmsApp app : apps){
			appIds.add(app.getId());
			if(app.getId().equals(appId)){
				flag = false;
				searchable.addSearchParam("application.id_eq", appId);
				break;
			}
		}
		if(flag)
		searchable.addSearchParam("application.id_in", appIds);
		
		searchable.addSort(Direction.ASC, "menuOrder");
		searchable.addSearchParam("menuName_like", name);
		List<UmsMenuResources> list = umsMenuResFacade.listUmsMenuResources(searchable);
		ListVO<ResourceTreeVO> listVO = new ListVO<>(list, ResourceTreeVO.class);
		return listVO;
	}
	
	private ListVO<ResourceTreeVO> getFromUumsUserController(String name, String appId, String username){
		Searchable searchable = Searchable.newSearchable();
		UmsUser user = umsUserFacade.findByUsername(username);
		List<UmsApp> appList = umsAppFacade.findAppByUserRoleRelation(user.getId(), FaySysRoleConstant.MANAGE_ROLE_SN_PREFIX);
		List<String> appIds = new ArrayList<>();
		boolean flag = true;
		for(UmsApp app : appList){
			appIds.add(app.getId());
			if(app.getId().equals(appId)){
				flag = false;
				searchable.addSearchParam("application.id_eq", appId);
				break;
			}
		}
		if(flag)
		searchable.addSearchParam("application.id_in", appIds);
		searchable.addSort(Direction.ASC, "controllerOrder");
		searchable.addSearchParam("controllerName_like", name);
		List<UmsControllerResources> list = umsControllerResFacade.listUmsControllerRes(searchable);
		ListVO<ResourceTreeVO> listVO = new ListVO<>(list, ResourceTreeVO.class);
		return listVO;
	}
}
