package com.rjsoft.uums.facade.resource.service.impl;

import com.rjsoft.common.model.search.Searchable;
import com.rjsoft.uums.core.app.service.UmsAppService;
import com.rjsoft.uums.core.resource.service.UmsButtonService;
import com.rjsoft.uums.core.resource.service.UmsControllerResService;
import com.rjsoft.uums.core.resource.service.UmsMenuResService;
import com.rjsoft.uums.facade.app.entity.UmsApp;
import com.rjsoft.uums.facade.resource.entity.UmsButton;
import com.rjsoft.uums.facade.resource.entity.UmsControllerResources;
import com.rjsoft.uums.facade.resource.entity.UmsMenuResources;
import com.rjsoft.uums.facade.resource.exception.MenuSnExistsException;
import com.rjsoft.uums.facade.resource.exception.ResourceException;
import com.rjsoft.uums.facade.resource.service.UmsMenuResFacade;
import com.rjsoft.uums.facade.user.entity.UmsUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service("umsMenuResFacadeImpl")
public class UmsMenuResFacadeImpl implements UmsMenuResFacade {
	
	@Autowired
	private UmsMenuResService umsMenuResService;
	@Autowired
	private UmsControllerResService umsControllerResService;
	@Autowired
	private UmsButtonService umsButtonService;
	@Autowired
	private UmsAppService umsAppService;

	@Override
	public UmsMenuResources create(UmsMenuResources umsMenuResources) throws MenuSnExistsException {
		UmsApp app  = umsMenuResources.getApplication();
		String appId = null;
		if(app != null) appId = app.getId();
		UmsMenuResources parent = umsMenuResources.getParent();
		String parentId = null;
		if(parent != null) parentId = parent.getId();
		if(appId != null){
			UmsApp _app = umsAppService.getOne(appId);
			if(_app == null){
				throw new ResourceException("menu.app.not.exited", null);
			}
		}
		if(parentId == null) {
			if(appId == null){
				throw new ResourceException("menu.app.is.null", null);
			}
		}else{
			UmsMenuResources _parent = umsMenuResService.getOne(parentId);
			if(_parent == null){
				throw new ResourceException("menu.parent.not.exited", null);
			} else {
				if(appId == null){
					UmsApp parentApp = _parent.getApplication();
					if(parentApp != null) {
						appId = parentApp.getId();
						app = new UmsApp();
						app.setId(appId);
						umsMenuResources.setApplication(app);
					}
				}
			}
		}
		return umsMenuResService.saveMenu(umsMenuResources);
	}

	@Override
	public UmsMenuResources update(UmsMenuResources umsMenuResources) {
		String id = umsMenuResources.getId();
		UmsMenuResources oMenu = umsMenuResService.getOne(id);
		UmsApp oApp = oMenu.getApplication();
		UmsApp cApp = umsMenuResources.getApplication();
		if(oApp != null && cApp != null && !(oApp.getId().equals(cApp.getId()))){
			updateChildren(id, cApp);
		}
		return umsMenuResService.update(umsMenuResources);
	}
	
	private void updateChildren(String id, UmsApp app){
		Searchable searchable = Searchable.newSearchable();
		searchable.addSearchParam("parent.id_eq", id);
		List<UmsMenuResources> umrs = umsMenuResService.findAllWithNoPageNoSort(searchable);
		for(UmsMenuResources menu : umrs){
			menu.setApplication(app);
			umsMenuResService.update(menu);
			Searchable cSearchable = Searchable.newSearchable();
			cSearchable.addSearchParam("parent.id_eq", id);
			List<UmsControllerResources> ucrs = umsControllerResService.findAllWithNoPageNoSort(cSearchable);
			for(UmsControllerResources ucr : ucrs){
				ucr.setApplication(app);
				umsControllerResService.update(ucr);
			}
			updateChildren(id, app);
		}
	}

	@Override
	public void deleteMenu(String... ids) {
		umsMenuResService.deleteMenu(ids);
	}

	@Override
	public void assignMenuButton(String menuId, String buttonIds) {
		umsMenuResService.addMenuButton(menuId, buttonIds);
	}
	
	@Override
	public Set<UmsMenuResources> getMenusByUserAndAppSn(UmsUser user, String appSn, Short isAvaiable){
		return umsMenuResService.getMenusByUserAndAppSn(user, appSn, isAvaiable);
	}

	@Override
	public List<UmsMenuResources> listUmsMenuResources(Searchable searchable) {
		return umsMenuResService.findAllWithSort(searchable);
	}
	
	@Override
	public Page<UmsMenuResources> listPageUmsMenuResources(Searchable searchable) {
		return umsMenuResService.findAll(searchable);
	}

	@Override
	public UmsButton create(UmsButton umsButton) {
		return umsButtonService.save(umsButton);
	}

	@Override
	public UmsButton update(UmsButton umsButton) {
		return umsButtonService.update(umsButton);
	}

	@Override
	public void deleteButton(String... ids) {
		umsButtonService.deleteButton(ids);
	}

	public UmsMenuResources findById(String id) {
		return umsMenuResService.getOne(id);
	}

	public UmsMenuResources updAvailable(String id, Short isAvailable) {
		UmsMenuResources mr = umsMenuResService.getOne(id);
		mr.setIsAvailable(isAvailable);
		mr = umsMenuResService.update(mr);
		return mr;
	}
}
