package com.rjsoft.uums.api.service.user.impl;

import com.rjsoft.common.entity.enums.AvailableEnum;
import com.rjsoft.common.model.search.Searchable;
import com.rjsoft.common.vo.PageVO;
import com.rjsoft.uums.api.constant.FayOrgRoleConstant;
import com.rjsoft.uums.api.constant.FaySysRoleConstant;
import com.rjsoft.uums.api.constant.FayUserConstant;
import com.rjsoft.uums.api.controller.vo.person.PersonVO;
import com.rjsoft.uums.api.controller.vo.user.PositionUserVO;
import com.rjsoft.uums.api.service.orgRole.OrgRoleService;
import com.rjsoft.uums.api.service.role.RoleService;
import com.rjsoft.uums.api.service.user.PositionUserService;
import com.rjsoft.uums.api.service.user.UserService;
import com.rjsoft.uums.facade.position.entity.UmsPosition;
import com.rjsoft.uums.facade.position.entity.UmsUserPositionRelation;
import com.rjsoft.uums.facade.position.service.UmsPositionFacade;
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
public class PositionUserServiceImpl implements PositionUserService {

    @Autowired
    private UserService userService;

    @Resource
    private UmsPositionFacade umsPositionFacade;

    @Autowired
    private RoleService roleService;

    @Autowired
    private OrgRoleService orgRoleService;

    @Override
    public PageVO<PositionUserVO> getUser(String nickname, String username, String positionId, int number, int size, String currentUsername, List<String> currentUserRoleSn) {
        PageVO<PositionUserVO> pageVO = null;
        if (Arrays.asList(FayUserConstant.SUPER_MANAGE_USERNAME).contains(currentUsername) || roleService.validate(currentUsername, FaySysRoleConstant.SUPER_MANAGE_ROLE_SN)) {
            pageVO = getPositionUser(nickname, username, positionId, number, size);
        } else if (orgRoleService.validate(currentUsername, FayOrgRoleConstant.ORG_SUPER_MANAGE_ROLE_SN)) {
            pageVO = getPositionUserByUnderOrgs(nickname, username, positionId, number, size, currentUsername);
        } else {
            pageVO = getDefaultPositionUser(nickname, username, positionId, number, size, currentUsername);
        }
        return pageVO;
    }

    @Override
    public PageVO<PersonVO> getPerson(String name, String sn, String positionId, int number, int size, String currentUsername, List<String> currentUserRoleSn) {
        PageVO<PersonVO> pageVO = null;
        if (Arrays.asList(FayUserConstant.SUPER_MANAGE_USERNAME).contains(currentUsername) || roleService.validate(currentUsername, FaySysRoleConstant.SUPER_MANAGE_ROLE_SN)) {
            pageVO = getPersonFromPositionUser(name, sn, positionId, number, size);
        } else if (orgRoleService.validate(currentUsername, FayOrgRoleConstant.ORG_SUPER_MANAGE_ROLE_SN)) {
            pageVO = getPersonFromPositionUserByUnderOrgs(name, sn, positionId, number, size, currentUsername);
        } else {
            pageVO = getDefaultPersonFromPositionUser(name, sn, positionId, number, size, currentUsername);
        }
        return pageVO;
    }

    private PageVO<PositionUserVO> getPositionUser(String nickname, String username, String positionId, int number, int size) {
        Pageable page = PageRequest.of(number, size);
        Searchable searchable = Searchable.newSearchable();
        searchable.setPage(page);
        searchable.addSearchParam("user.nickname_like", nickname);
        searchable.addSearchParam("user.username_like", username);
        Sort sort = new Sort(Direction.DESC, "user.createDate");
        searchable.addSort(sort);
        searchable.addSearchParam("position.id_eq", positionId);
        Page<UmsUserPositionRelation> pagePositionUser = umsPositionFacade.listUmsUserPositionRelationPage(searchable);
        return new PageVO<>(pagePositionUser, PositionUserVO.class);
    }

    private PageVO<PersonVO> getPersonFromPositionUser(String name, String sn, String positionId, int number, int size) {
        Pageable page = PageRequest.of(number, size);
        Searchable searchable = Searchable.newSearchable();
        searchable.setPage(page);
        searchable.addSearchParam("user.person.name_like", name);
        searchable.addSearchParam("user.person.sn_like", sn);
        Sort sort = new Sort(Direction.DESC, "user.createDate");
        searchable.addSort(sort);
        searchable.addSearchParam("position.id_eq", positionId);
        Page<UmsUserPositionRelation> pagePositionUser = umsPositionFacade.listUmsUserPositionRelationPage(searchable);
        return new PageVO<>(pagePositionUser, PersonVO.class);
    }

    private PageVO<PositionUserVO> getPositionUserByUnderOrgs(String nickname, String username, String positionId, int number, int size, String currentUsername) {
        Pageable page = PageRequest.of(number, size);
        Searchable searchable = Searchable.newSearchable();
        searchable.setPage(page);
        searchable.addSearchParam("user.nickname_like", nickname);
        searchable.addSearchParam("user.username_like", username);
        Sort sort = new Sort(Direction.DESC, "user.createDate");
        searchable.addSort(sort);

        List<String> orgIds = userService.getOrgIdsForManageByLoginUser(currentUsername);

        Searchable positionSearchable = Searchable.newSearchable();
        positionSearchable.addSearchParam("orgId_in", orgIds);
        positionSearchable.addSearchParam("isAvailable_eq", AvailableEnum.TRUE.getValue());
        List<UmsPosition> userPositionList = umsPositionFacade.list(positionSearchable);
        for (UmsPosition position : userPositionList) {
            if (position.getId().equals(positionId)) {
                searchable.addSearchParam("position.id_eq", positionId);
                Page<UmsUserPositionRelation> pagePositionUser = umsPositionFacade.listUmsUserPositionRelationPage(searchable);
                return new PageVO<>(pagePositionUser, PositionUserVO.class);
            }
        }
        return new PageVO<>(new ArrayList<>(), PositionUserVO.class);
    }

    private PageVO<PersonVO> getPersonFromPositionUserByUnderOrgs(String name, String sn, String positionId, int number, int size, String currentUsername) {
        Pageable page = PageRequest.of(number, size);
        Searchable searchable = Searchable.newSearchable();
        searchable.setPage(page);
        searchable.addSearchParam("user.person.name_like", name);
        searchable.addSearchParam("user.person.sn_like", sn);
        Sort sort = new Sort(Direction.DESC, "user.createDate");
        searchable.addSort(sort);

        List<String> orgIds = userService.getOrgIdsForManageByLoginUser(currentUsername);

        Searchable positionSearchable = Searchable.newSearchable();
        positionSearchable.addSearchParam("orgId_in", orgIds);
        positionSearchable.addSearchParam("isAvailable_eq", AvailableEnum.TRUE.getValue());
        List<UmsPosition> userPositionList = umsPositionFacade.list(positionSearchable);
        for (UmsPosition position : userPositionList) {
            if (position.getId().equals(positionId)) {
                searchable.addSearchParam("position.id_eq", positionId);
                Page<UmsUserPositionRelation> pagePositionUser = umsPositionFacade.listUmsUserPositionRelationPage(searchable);
                return new PageVO<>(pagePositionUser, PersonVO.class);
            }
        }
        return new PageVO<>(new ArrayList<>(), PersonVO.class);
    }

    private PageVO<PositionUserVO> getDefaultPositionUser(String nickname, String username, String positionId, int number, int size, String currentUsername) {
        Pageable page = PageRequest.of(number, size);
        Searchable searchable = Searchable.newSearchable();
        searchable.setPage(page);
        searchable.addSearchParam("user.nickname_like", nickname);
        searchable.addSearchParam("user.username_like", username);
        searchable.addSearchParam("user.username_eq", currentUsername);
        Sort sort = new Sort(Direction.DESC, "user.createDate");
        searchable.addSort(sort);
        searchable.addSearchParam("position.id_eq", positionId);
        Page<UmsUserPositionRelation> pagePositionUser = umsPositionFacade.listUmsUserPositionRelationPage(searchable);
        return new PageVO<>(pagePositionUser, PositionUserVO.class);
    }

    private PageVO<PersonVO> getDefaultPersonFromPositionUser(String name, String sn, String positionId, int number, int size, String currentUsername) {
        Pageable page = PageRequest.of(number, size);
        Searchable searchable = Searchable.newSearchable();
        searchable.setPage(page);
        searchable.addSearchParam("user.person.name_like", name);
        searchable.addSearchParam("user.person.sn_like", sn);
        searchable.addSearchParam("user.username_eq", currentUsername);
        Sort sort = new Sort(Direction.DESC, "user.createDate");
        searchable.addSort(sort);
        searchable.addSearchParam("position.id_eq", positionId);
        Page<UmsUserPositionRelation> pagePositionUser = umsPositionFacade.listUmsUserPositionRelationPage(searchable);
        return new PageVO<>(pagePositionUser, PersonVO.class);
    }
}
