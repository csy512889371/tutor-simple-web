package com.rjsoft.uums.core.resource.service;

import com.rjsoft.common.entity.enums.AvailableEnum;
import com.rjsoft.common.service.BaseService;
import com.rjsoft.uums.core.auth.repository.UmsAclRepository;
import com.rjsoft.uums.core.department.repository.UmsDepartmentRepository;
import com.rjsoft.uums.core.department.repository.UmsUserDepartmentRelationRepository;
import com.rjsoft.uums.core.org.repository.UmsUserOrgRelationRepository;
import com.rjsoft.uums.core.orgRole.repository.UmsUserOrgRoleRelationRepository;
import com.rjsoft.uums.core.position.repository.UmsPositionRepository;
import com.rjsoft.uums.core.position.repository.UmsUserPositionRelationRepository;
import com.rjsoft.uums.core.resource.repository.UmsControllerResRepository;
import com.rjsoft.uums.core.resource.repository.UmsMenuButtonRepository;
import com.rjsoft.uums.core.resource.repository.UmsMenuResRepository;
import com.rjsoft.uums.core.role.repository.UmsUserRoleRelationRepository;
import com.rjsoft.uums.facade.Principal;
import com.rjsoft.uums.facade.Resource;
import com.rjsoft.uums.facade.app.entity.UmsApp;
import com.rjsoft.uums.facade.department.entity.UmsDepartment;
import com.rjsoft.uums.facade.position.entity.UmsPosition;
import com.rjsoft.uums.facade.resource.entity.UmsMenuButton;
import com.rjsoft.uums.facade.resource.entity.UmsMenuResources;
import com.rjsoft.uums.facade.resource.exception.MenuSnExistsException;
import com.rjsoft.uums.facade.user.entity.UmsUser;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service("umsMenuService")
public class UmsMenuResService extends BaseService<UmsMenuResources, String> {


    @Autowired
    private UmsMenuButtonRepository umsMenuButtonRepository;
    @Autowired
    private UmsMenuResRepository umsMenuResRepository;
    @Autowired
    private UmsControllerResService umsControllerResService;
    @Autowired
    private UmsControllerResRepository umsControllerResRepository;
    @Autowired
    private UmsDepartmentRepository umsDepartmentRepository;
    @Autowired
    private UmsPositionRepository umsPositionRepository;
    @Autowired
    private UmsUserOrgRelationRepository umsUserOrgRelationRepository;
    @Autowired
    private UmsUserDepartmentRelationRepository umsUserDepartmentRelationRepository;
    @Autowired
    private UmsUserPositionRelationRepository umsUserPositionRelationRepository;
    @Autowired
    private UmsUserRoleRelationRepository umsUserRoleRelationRepository;
    @Autowired
    private UmsUserOrgRoleRelationRepository umsUserOrgRoleRelationRepository;
    @Autowired
    private UmsAclRepository umsAclRepository;

    /**
     * 保存菜单
     * @param menu
     */
    public UmsMenuResources saveMenu(UmsMenuResources menu){
        if(findMenuByMenuSnAndAppSn(menu.getMenuSn(),menu.getApplication())!=null){
            throw new MenuSnExistsException();
        }
        return save(menu);
    }
    /**
     * 根据菜单sn和应用系统获取菜单
     * @param menuSn
     * @param app
     * @return
     */
    public UmsMenuResources findMenuByMenuSnAndAppSn(String menuSn,UmsApp app){
        return umsMenuResRepository.findMenuByMenuSnAndAppSn(menuSn,app);
    }

    /**
     * 添加菜单按钮
     * @param menuId
     * @param buttonIds
     */
    public void addMenuButton(String menuId,String buttonIds){
        if(StringUtils.isEmpty(buttonIds)){
            umsMenuButtonRepository.deleteMenuButtonByMenuId(menuId);
        }else{
            List<UmsMenuButton> menuButtons = new ArrayList<UmsMenuButton>();
            umsMenuButtonRepository.deleteMenuButtonByMenuId(menuId);
            for(String buttonId:buttonIds.split(",")){
                UmsMenuButton umsMenuButton = new UmsMenuButton();
                umsMenuButton.setMenuId(menuId);
                umsMenuButton.setButtonId(buttonId);
                menuButtons.add(umsMenuButton);
            }
            umsMenuButtonRepository.saveAll(menuButtons);
        }
    }

    /**
     * 根据用户和appSn获取所有可用菜单
     * @param user
     * @param appSn
     * @return
     */
    public Set<UmsMenuResources> getMenusByUserAndAppSn(UmsUser user, String appSn, Short isAvaiable){
        Set<UmsMenuResources> menuSet = new TreeSet<UmsMenuResources>();
        String userId = user.getId();

        List<String> orgIds = umsUserOrgRelationRepository.findOrgIds(userId, AvailableEnum.TRUE.getValue());
        List<String> departmentIds = umsUserDepartmentRelationRepository.findDepartmentIds(userId,AvailableEnum.TRUE.getValue());
        for(String departmentId : departmentIds){
            UmsDepartment department = umsDepartmentRepository.getOne(departmentId);
            if(department != null) orgIds.add(department.getOrgId());
        }
        List<String> posIds = umsUserPositionRelationRepository.findPositionIds(userId, AvailableEnum.TRUE.getValue());
        for(String posId : posIds){
            UmsPosition position = umsPositionRepository.getOne(posId);
            if(position != null) {
                departmentIds.add(position.getDepartmentId());
                orgIds.add(position.getOrgId());
            }
        }
        List<String> roleIds = umsUserRoleRelationRepository.findRoleIds(userId, AvailableEnum.TRUE.getValue());
        List<String> orgRoleIds = umsUserOrgRoleRelationRepository.findRoleIds(userId, AvailableEnum.TRUE.getValue());
        List<String> userIds = new ArrayList<>();
        userIds.add(userId);
        List<UmsMenuResources> menus = new ArrayList<>();
        List<UmsMenuResources> userMenus = umsMenuResRepository.getMenuList(Resource.RESOURCE_MENU, userIds, Principal.PRINCIPAL_USER, appSn,isAvaiable);
        menus.addAll(userMenus);
        if(orgIds!=null&&orgIds.size()>0){
            List<UmsMenuResources> orgMenus = umsMenuResRepository.getMenuList(Resource.RESOURCE_MENU, orgIds, Principal.PRINCIPAL_ORG, appSn,isAvaiable);
            menus.addAll(orgMenus);
        }
        if(departmentIds!=null&&departmentIds.size()>0){
            List<UmsMenuResources> departmentMenus = umsMenuResRepository.getMenuList(Resource.RESOURCE_MENU, departmentIds, Principal.PRINCIPAL_DEPARTMENT, appSn,isAvaiable);
            menus.addAll(departmentMenus);
        }
        if(posIds!=null&&posIds.size()>0){
            List<UmsMenuResources> positionMenus = umsMenuResRepository.getMenuList(Resource.RESOURCE_MENU, posIds, Principal.PRINCIPAL_POSITION, appSn,isAvaiable);
            menus.addAll(positionMenus);
        }
        if(roleIds!=null&&roleIds.size()>0){
            List<UmsMenuResources> roleMenus = umsMenuResRepository.getMenuList(Resource.RESOURCE_MENU, roleIds, Principal.PRINCIPAL_ROLE, appSn,isAvaiable);
            menus.addAll(roleMenus);
        }
        if(orgRoleIds!=null&&orgRoleIds.size()>0){
            List<UmsMenuResources> orgRoleMenus = umsMenuResRepository.getMenuList(Resource.RESOURCE_MENU, orgRoleIds, Principal.PRINCIPAL_ORG_ROLE, appSn,isAvaiable);
            menus.addAll(orgRoleMenus);
        }
        for(UmsMenuResources umr : menus){
            menuSet.add(umr);
        }
        return menuSet;
    }

    /**
     * 获取主体id和主体类型对应的所有可用菜单
     */
//	public List<UmsMenuResources> getAvailableMenusByPrincipalAndAppSn(String pid,String ptype,String appSn){
//		return umsMenuResRepository.getMenuList(Resource.RESOURCE_MENU, pid, ptype, appSn,AvailableEnum.TRUE.getValue());
//	}

    /**
     * 根据appSn获取所有可用菜单
     */
    public List<UmsMenuResources> getAvailableMenusByAppSn(String appSn){
        return umsMenuResRepository.getMenusByAppSn(appSn, AvailableEnum.TRUE.getValue());
    }

    public void deleteMenu(String... ids){
        for(String id:ids){
            umsAclRepository.clearResourceAcl(id, Resource.RESOURCE_MENU);
            List<String> mChildIds = umsMenuResRepository.findMenuByParentId(id);
            if(mChildIds != null && mChildIds.size()>0){
                deleteMenu(mChildIds.toArray(new String[mChildIds.size()]));
            }
            List<String> cChildIds = umsControllerResRepository.findControllerByMenuId(id);
            if(cChildIds != null && cChildIds.size()>0){
                umsControllerResService.deleteControllerRes(cChildIds.toArray(new String[cChildIds.size()]));
            }
            delete(id);
        }
    }

    public List<UmsMenuResources> findMenuByIds(Set<String> menuIds){
        return umsMenuResRepository.findAllById(menuIds);
    }

    public void deleteByAppIds(List<String> appIds){
        umsMenuResRepository.deleteByAppIds(appIds);
    }

}
