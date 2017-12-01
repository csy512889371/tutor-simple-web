package com.rjsoft.uums.api.service.user.impl;

import com.rjsoft.common.model.search.Searchable;
import com.rjsoft.common.vo.PageVO;
import com.rjsoft.uums.api.constant.FayOrgRoleConstant;
import com.rjsoft.uums.api.constant.FaySysRoleConstant;
import com.rjsoft.uums.api.constant.FayUserConstant;
import com.rjsoft.uums.api.controller.vo.person.PersonVO;
import com.rjsoft.uums.api.controller.vo.user.OrgUserVO;
import com.rjsoft.uums.api.service.orgRole.OrgRoleService;
import com.rjsoft.uums.api.service.role.RoleService;
import com.rjsoft.uums.api.service.user.OrgUserService;
import com.rjsoft.uums.api.service.user.UserService;
import com.rjsoft.uums.facade.org.entity.UmsUserOrgRelation;
import com.rjsoft.uums.facade.org.service.UmsOrgFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class OrgUserServiceImpl implements OrgUserService {

    @Autowired
    private UserService userService;

    @Resource
    private UmsOrgFacade umsOrgFacade;

    @Autowired
    private RoleService roleService;

    @Autowired
    private OrgRoleService orgRoleService;

    @Override
    public PageVO<OrgUserVO> getUser(String nickname, String username, String orgId, int number, int size, String currentUsername, List<String> currentUserRoleSn) {
        PageVO<OrgUserVO> pageVO = null;
        if (Arrays.asList(FayUserConstant.SUPER_MANAGE_USERNAME).contains(currentUsername) || roleService.validate(currentUsername, FaySysRoleConstant.SUPER_MANAGE_ROLE_SN)) {
            pageVO = getOrgUser(nickname, username, orgId, number, size);
        } else if (orgRoleService.validate(currentUsername, FayOrgRoleConstant.ORG_SUPER_MANAGE_ROLE_SN)) {
            pageVO = getOrgUserByUnderOrgs(nickname, username, orgId, number, size, currentUsername);
        } else {
            pageVO = getDefaultOrgUserByUnderOrgs(nickname, username, orgId, number, size, currentUsername);
        }
        return pageVO;
    }

    @Override
    public PageVO<PersonVO> getPerson(String name, String sn, String orgId, int number, int size, String currentUsername, List<String> currentUserRoleSn) {
        PageVO<PersonVO> pageVO = null;
        if (Arrays.asList(FayUserConstant.SUPER_MANAGE_USERNAME).contains(currentUsername) || roleService.validate(currentUsername, FaySysRoleConstant.SUPER_MANAGE_ROLE_SN)) {
            pageVO = getPersonFromOrgUser(name, sn, orgId, number, size);
        } else if (orgRoleService.validate(currentUsername, FayOrgRoleConstant.ORG_SUPER_MANAGE_ROLE_SN)) {
            pageVO = getPersonFromOrgUserByUnderOrgs(name, sn, orgId, number, size, currentUsername);
        } else {
            pageVO = getDefaultPersonFromOrgUserByUnderOrgs(name, sn, orgId, number, size, currentUsername);
        }
        return pageVO;
    }

    private PageVO<OrgUserVO> getOrgUser(String nickname, String username, String orgId, int number, int size) {
        Pageable page = PageRequest.of(number, size);
        Searchable searchable = Searchable.newSearchable();
        searchable.setPage(page);
        searchable.addSearchParam("user.nickname_like", nickname);
        searchable.addSearchParam("user.username_like", username);
        Sort sort = new Sort(Direction.DESC, "user.createDate");
        searchable.addSort(sort);
        searchable.addSearchParam("org.id_eq", orgId);
        Page<UmsUserOrgRelation> pageOrgUser = umsOrgFacade.listUmsUserOrgRelationPage(searchable);
        return new PageVO<>(pageOrgUser, OrgUserVO.class);
    }

    private PageVO<PersonVO> getPersonFromOrgUser(String name, String sn, String orgId, int number, int size) {
        Pageable page = PageRequest.of(number, size);
        Searchable searchable = Searchable.newSearchable();
        searchable.setPage(page);
        searchable.addSearchParam("user.person.name_like", name);
        searchable.addSearchParam("user.person.sn_like", sn);
        Sort sort = new Sort(Direction.DESC, "user.createDate");
        searchable.addSort(sort);
        searchable.addSearchParam("org.id_eq", orgId);
        Page<UmsUserOrgRelation> pageOrgUser = umsOrgFacade.listUmsUserOrgRelationPage(searchable);
        return new PageVO<>(pageOrgUser, PersonVO.class);
    }

    private PageVO<OrgUserVO> getOrgUserByUnderOrgs(String nickname, String username, String orgId, int number, int size, String currentUsername) {
        Pageable page = PageRequest.of(number, size);
        Searchable searchable = Searchable.newSearchable();
        searchable.setPage(page);
        searchable.addSearchParam("user.nickname_like", nickname);
        searchable.addSearchParam("user.username_like", username);
        Sort sort = new Sort(Direction.DESC, "user.createDate");
        searchable.addSort(sort);

        List<String> orgIds = userService.getOrgIdsForManageByLoginUser(currentUsername);

        if (orgId == null) {
            searchable.addSearchParam("org.id_in", orgIds);
            Page<UmsUserOrgRelation> pageOrgUser = umsOrgFacade.listUmsUserOrgRelationPage(searchable);
            return new PageVO<>(pageOrgUser, OrgUserVO.class);
        } else if (orgIds.contains(orgId)) {
            searchable.addSearchParam("org.id_eq", orgId);
            Page<UmsUserOrgRelation> pageOrgUser = umsOrgFacade.listUmsUserOrgRelationPage(searchable);
            return new PageVO<>(pageOrgUser, OrgUserVO.class);
        } else {
            return new PageVO<>(new ArrayList<>(), OrgUserVO.class);
        }
    }

    private PageVO<PersonVO> getPersonFromOrgUserByUnderOrgs(String name, String sn, String orgId, int number, int size, String currentUsername) {
        Pageable page = PageRequest.of(number, size);
        Searchable searchable = Searchable.newSearchable();
        searchable.setPage(page);
        searchable.addSearchParam("user.person.name_like", name);
        searchable.addSearchParam("user.person.sn_like", sn);
        Sort sort = new Sort(Direction.DESC, "user.createDate");
        searchable.addSort(sort);

        List<String> orgIds = userService.getOrgIdsForManageByLoginUser(currentUsername);

        if (orgId == null) {
            searchable.addSearchParam("org.id_in", orgIds);
            Page<UmsUserOrgRelation> pageOrgUser = umsOrgFacade.listUmsUserOrgRelationPage(searchable);
            return new PageVO<>(pageOrgUser, PersonVO.class);
        } else if (orgIds.contains(orgId)) {
            searchable.addSearchParam("org.id_eq", orgId);
            Page<UmsUserOrgRelation> pageOrgUser = umsOrgFacade.listUmsUserOrgRelationPage(searchable);
            return new PageVO<>(pageOrgUser, PersonVO.class);
        } else {
            return new PageVO<>(new ArrayList<>(), PersonVO.class);
        }
    }

    private PageVO<OrgUserVO> getDefaultOrgUserByUnderOrgs(String nickname, String username, String orgId, int number, int size, String currentUsername) {
        Pageable page = PageRequest.of(number, size);
        Searchable searchable = Searchable.newSearchable();
        searchable.setPage(page);
        searchable.addSearchParam("user.nickname_like", nickname);
        searchable.addSearchParam("user.username_like", username);
        searchable.addSearchParam("user.username_eq", currentUsername);
        Sort sort = new Sort(Direction.DESC, "user.createDate");
        searchable.addSort(sort);
        searchable.addSearchParam("org.id_eq", orgId);
        Page<UmsUserOrgRelation> pageOrgUser = umsOrgFacade.listUmsUserOrgRelationPage(searchable);
        return new PageVO<>(pageOrgUser, OrgUserVO.class);
    }

    private PageVO<PersonVO> getDefaultPersonFromOrgUserByUnderOrgs(String name, String sn, String orgId, int number, int size, String currentUsername) {
        Pageable page = PageRequest.of(number, size);
        Searchable searchable = Searchable.newSearchable();
        searchable.setPage(page);
        searchable.addSearchParam("user.person.name_like", name);
        searchable.addSearchParam("user.person.sn_like", name);
        searchable.addSearchParam("user.username_eq", currentUsername);
        Sort sort = new Sort(Direction.DESC, "user.createDate");
        searchable.addSort(sort);
        searchable.addSearchParam("org.id_eq", orgId);
        Page<UmsUserOrgRelation> pageOrgUser = umsOrgFacade.listUmsUserOrgRelationPage(searchable);
        return new PageVO<>(pageOrgUser, PersonVO.class);
    }
}
