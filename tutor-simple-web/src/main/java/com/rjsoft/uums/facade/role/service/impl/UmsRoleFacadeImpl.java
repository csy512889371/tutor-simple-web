package com.rjsoft.uums.facade.role.service.impl;

import com.rjsoft.common.model.search.Searchable;
import com.rjsoft.uums.core.role.service.UmsRoleService;
import com.rjsoft.uums.core.role.service.UmsUserRoleRelationService;
import com.rjsoft.uums.facade.role.entity.UmsRole;
import com.rjsoft.uums.facade.role.entity.UmsUserRoleRelation;
import com.rjsoft.uums.facade.role.exception.RoleSnExistsException;
import com.rjsoft.uums.facade.role.service.UmsRoleFacade;
import com.rjsoft.uums.facade.user.entity.UmsUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("umsRoleFacadeImpl")
public class UmsRoleFacadeImpl implements UmsRoleFacade {

    @Autowired
    private UmsRoleService umsRoleService;
    @Autowired
    private UmsUserRoleRelationService umsUserRoleRelationService;

    @Override
    public UmsRole create(UmsRole umsRole) throws RoleSnExistsException {
        return umsRoleService.saveRole(umsRole);
    }

    @Override
    public UmsRole update(UmsRole umsRole) {
        return umsRoleService.update(umsRole);
    }

    @Override
    public void delete(String... ids) {
        List<String> roleIds = new ArrayList<>();
        for (String id : ids) {
            roleIds.add(id);
        }
        umsUserRoleRelationService.deleteByRoleIds(roleIds);
        umsRoleService.deleteRole(ids);
    }

    @Override
    public UmsRole getBySn(String sn) {
        return umsRoleService.findByRoleSn(sn);
    }

    @Override
    public UmsRole getById(String id) {
        return umsRoleService.getOne(id);
    }

    @Override
    public Page<UmsRole> listPage(Searchable searchable) {
        return umsRoleService.findAll(searchable);
    }

    @Override
    public List<UmsRole> list(Searchable searchable) {
        return umsRoleService.findAllWithNoPageNoSort(searchable);
    }

    public UmsRole updAvailable(String id, Short isAvailable) {
        UmsRole role = umsRoleService.getOne(id);
        role.setIsAvailable(isAvailable);
        role = umsRoleService.update(role);
        return role;
    }

    @Override
    public void clearUserRoleRelation(String roleId, List<String> userIds) {
        umsRoleService.clearUserRoleRelation(roleId, userIds);
    }

    @Override
    public Page<UmsUser> findUserByRoleId(String roleId, String nickname, String username, Pageable page) {
        return umsRoleService.findUserByRoleId(roleId, nickname, username, page);
    }

    @Override
    public Page<UmsUser> findNotUserByRoleId(String roleId, String nickname, String username, Pageable page) {
        return umsRoleService.findNotUserByRoleId(roleId, nickname, username, page);
    }

    @Override
    public void buildUserRoleRelation(String roleId, String[] userIds) {
        umsRoleService.buildUserRoleRelation(roleId, userIds);
    }

    @Override
    public Page<UmsUserRoleRelation> listUmsUserRoleRelationPage(Searchable searchable) {
        return umsUserRoleRelationService.findAll(searchable);
    }

    @Override
    public List<UmsUserRoleRelation> listUmsUserRoleRelation(Searchable searchable) {
        return umsUserRoleRelationService.findAllWithSort(searchable);
    }

    @Override
    public void deleteByApp(List<String> appIds) {
        umsRoleService.deleteByAppIdIn(appIds);
    }
}
