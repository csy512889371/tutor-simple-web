package com.rjsoft.uums.core.role.service;

import com.google.common.collect.Sets;
import com.rjsoft.common.service.BaseService;
import com.rjsoft.uums.core.auth.repository.UmsAclRepository;
import com.rjsoft.uums.core.role.repository.UmsRoleRepository;
import com.rjsoft.uums.core.role.repository.UmsUserRoleRelationRepository;
import com.rjsoft.uums.facade.Principal;
import com.rjsoft.uums.facade.role.entity.UmsRole;
import com.rjsoft.uums.facade.role.entity.UmsUserRoleRelation;
import com.rjsoft.uums.facade.role.exception.RoleSnExistsException;
import com.rjsoft.uums.facade.user.entity.UmsUser;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service("umsRoleService")
public class UmsRoleService extends BaseService<UmsRole, String> {

    @Autowired
    private UmsUserRoleRelationRepository umsUserRoleRelationRepository;
    @Autowired
    private UmsRoleRepository umsRoleRepository;
    @Autowired
    private UmsAclRepository umsAclRepository;

    /**
     * 保存角色
     *
     * @param role
     */
    public UmsRole saveRole(UmsRole role) {
        if (findByRoleSn(role.getSn()) != null) {
            throw new RoleSnExistsException();
        }
        return save(role);
    }

    /**
     * 根据角色标识符查找角色
     *
     * @param roleSn
     * @return
     */
    public UmsRole findByRoleSn(String roleSn) {
        return umsRoleRepository.findByRoleSn(roleSn);
    }

    /**
     * 根据userId获取所有可用角色Sn
     *
     * @param userId
     * @param isAvailable
     * @return
     */
    public Set<String> findRoleIds(String userId, Short isAvailable) {
        Set<String> roleIds = Sets.newHashSet();
        roleIds.addAll(Sets.newHashSet(umsUserRoleRelationRepository.findRoleIds(userId, isAvailable)));
        return roleIds;
    }

    /**
     * 根据userId获取所有可用角色
     *
     * @param userId
     * @param isAvailable
     * @return
     */
    public Set<UmsRole> findRoles(String userId, Short isAvailable) {
        Set<UmsRole> roles = Sets.newHashSet();
        roles.addAll(Sets.newHashSet(umsUserRoleRelationRepository.findRoles(userId, isAvailable)));
        return roles;
    }

    /**
     * 添加用户角色关联
     *
     * @param roleId
     * @param userIds
     */
    public void buildUserRoleRelation(String roleId, String[] userIds) {
        if (ArrayUtils.isEmpty(userIds)) {
            return;
        } else {
            for (String userId : userIds) {
                if (StringUtils.isEmpty(userId)) {
                    continue;
                }
                UmsUserRoleRelation r = umsUserRoleRelationRepository.findByRoleIdAndUserId(roleId, userId);
                if (r == null) {
                    r = new UmsUserRoleRelation();
                    UmsRole role = new UmsRole();
                    role.setId(roleId);
                    UmsUser user = new UmsUser();
                    user.setId(userId);
                    r.setRole(role);
                    r.setUser(user);
                    umsUserRoleRelationRepository.save(r);
                }
            }
        }
    }

    /**
     * 删除角色
     *
     * @param roleIds
     */
    public void deleteRole(String... roleIds) {
        for (String roleId : roleIds) {
            umsAclRepository.clearPrincipalAcl(roleId, Principal.PRINCIPAL_ROLE);
            umsUserRoleRelationRepository.clearUserRoleRelation(roleId);
            delete(roleId);
        }
    }

    /**
     * 删除用户与角色关系
     *
     * @param roleId
     * @param userIds
     */
    public void clearUserRoleRelation(String roleId, List<String> userIds) {
        if (userIds == null || userIds.size() == 0) {
            return;
        } else {
            umsUserRoleRelationRepository.clearUserRoleRelation(roleId, userIds);
        }
    }

    /**
     * 根据角色id获取该角色下的所有用户
     *
     * @param roleId
     * @param nickname
     * @param username
     * @param page
     * @return
     */
    public Page<UmsUser> findUserByRoleId(String roleId, String nickname, String username, Pageable page) {
        return umsUserRoleRelationRepository.findUserByRoleId(roleId, nickname, username, page);
    }

    /**
     * 根据角色id获取该角色下以外的所有用户
     *
     * @param roleId
     * @param nickname
     * @param username
     * @param page
     * @return
     */
    public Page<UmsUser> findNotUserByRoleId(String roleId, String nickname, String username, Pageable page) {
        return umsUserRoleRelationRepository.findNotUserByRoleId(roleId, nickname, username, page);
    }

    public void deleteByAppIdIn(List<String> appIds) {
        umsUserRoleRelationRepository.deleteByAppIds(appIds);
        umsRoleRepository.deleteByAppIdIn(appIds);
    }


}
