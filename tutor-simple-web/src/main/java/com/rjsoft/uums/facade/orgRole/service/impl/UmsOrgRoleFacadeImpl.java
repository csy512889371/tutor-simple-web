package com.rjsoft.uums.facade.orgRole.service.impl;

import com.rjsoft.common.model.search.Searchable;
import com.rjsoft.uums.core.orgRole.service.UmsOrgRoleService;
import com.rjsoft.uums.core.orgRole.service.UmsUserOrgRoleRelationService;
import com.rjsoft.uums.facade.orgRole.entity.UmsOrgRole;
import com.rjsoft.uums.facade.orgRole.entity.UmsUserOrgRoleRelation;
import com.rjsoft.uums.facade.orgRole.exception.OrgRoleSnExistsException;
import com.rjsoft.uums.facade.orgRole.service.UmsOrgRoleFacade;
import com.rjsoft.uums.facade.user.entity.UmsUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("umsOrgRoleFacadeImpl")
public class UmsOrgRoleFacadeImpl implements UmsOrgRoleFacade {

    @Autowired
    private UmsOrgRoleService umsOrgRoleService;
    @Autowired
    private UmsUserOrgRoleRelationService umsUserOrgRoleRelationService;

    @Override
    public UmsOrgRole create(UmsOrgRole umsOrgRole) throws OrgRoleSnExistsException {
        return umsOrgRoleService.saveRole(umsOrgRole);
    }

    @Override
    public UmsOrgRole update(UmsOrgRole umsOrgRole) {
        return umsOrgRoleService.update(umsOrgRole);
    }

    @Override
    public void delete(String... ids) {
        umsOrgRoleService.deleteRole(ids);
    }

    @Override
    public UmsOrgRole getBySn(String sn) {
        return umsOrgRoleService.findByRoleSn(sn);
    }

    @Override
    public UmsOrgRole getById(String id) {
        return umsOrgRoleService.getOne(id);
    }

    @Override
    public Page<UmsOrgRole> listPage(Searchable searchable) {
        return umsOrgRoleService.findAll(searchable);
    }

    @Override
    public List<UmsOrgRole> list(Searchable searchable) {
        return umsOrgRoleService.findAllWithNoPageNoSort(searchable);
    }

    public UmsOrgRole updAvailable(String id, Short isAvailable) {
        UmsOrgRole role = umsOrgRoleService.getOne(id);
        role.setIsAvailable(isAvailable);
        role = umsOrgRoleService.update(role);
        return role;
    }

    @Override
    public void clearUserRoleRelation(String roleId, List<String> userIds) {
        umsOrgRoleService.clearUserRoleRelation(roleId, userIds);
    }

    @Override
    public Page<UmsUser> findUserByRoleId(String roleId, String nickname, String username, Pageable page) {
        return umsOrgRoleService.findUserByRoleId(roleId, nickname, username, page);
    }

    @Override
    public Page<UmsUser> findNotUserByRoleId(String roleId, String nickname, String username, Pageable page) {
        return umsOrgRoleService.findNotUserByRoleId(roleId, nickname, username, page);
    }

    @Override
    public void buildUserRoleRelation(String roleId, String[] userIds) {
        umsOrgRoleService.buildUserRoleRelation(roleId, userIds);
    }

    @Override
    public Page<UmsUserOrgRoleRelation> listUmsUserRoleRelationPage(Searchable searchable) {
        return umsUserOrgRoleRelationService.findAll(searchable);
    }

    @Override
    public List<UmsUserOrgRoleRelation> listUmsUserRoleRelation(Searchable searchable) {
        return umsUserOrgRoleRelationService.findAllWithSort(searchable);
    }

    @Override
    public void deleteByOrg(String[] orgIds) {
        umsOrgRoleService.deleteByOrgIdIn(orgIds);
    }

    @Override
    public Page<UmsUser> findUserByRoleIdAndNameAndSn(String roleId, String name, String sn, Pageable page) {
        return umsOrgRoleService.findUserByRoleIdAndNameAndSn(roleId, name, sn, page);
    }

    @Override
    public Page<UmsUser> findNotUserByRoleIdAndNameAndSn(String roleId, String name, String sn, Pageable page) {
        return umsOrgRoleService.findNotUserByRoleIdAndNameAndSn(roleId, name, sn, page);
    }
}
