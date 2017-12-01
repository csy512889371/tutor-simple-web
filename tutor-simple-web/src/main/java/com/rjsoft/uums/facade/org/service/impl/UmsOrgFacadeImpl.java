package com.rjsoft.uums.facade.org.service.impl;

import com.rjsoft.common.model.search.Searchable;
import com.rjsoft.uums.core.org.service.UmsOrgService;
import com.rjsoft.uums.core.org.service.UmsOrgTypeService;
import com.rjsoft.uums.core.org.service.UmsUserOrgRelationService;
import com.rjsoft.uums.facade.org.entity.*;
import com.rjsoft.uums.facade.org.exception.OrgTypeNotDeleteException;
import com.rjsoft.uums.facade.org.exception.OrgTypeSnExistsException;
import com.rjsoft.uums.facade.org.service.UmsOrgFacade;
import com.rjsoft.uums.facade.user.entity.UmsUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("umsOrgFacadeImpl")
public class UmsOrgFacadeImpl implements UmsOrgFacade {

    @Autowired
    private UmsOrgService umsOrgService;
    @Autowired
    private UmsUserOrgRelationService umsUserOrgRelationService;
    @Autowired
    private UmsOrgTypeService umsOrgTypeService;

    @Override
    public UmsOrg createUmsOrg(UmsOrg umsOrg) {
        return umsOrgService.save(umsOrg);
    }

    @Override
    public UmsOrg updateUmsOrg(UmsOrg umsOrg) {
        return umsOrgService.update(umsOrg);
    }

    @Override
    public void deleteUmsOrg(String... ids) {
        umsOrgService.deleteOrg(ids);
    }

    @Override
    public Page<UmsOrg> listUmsOrgPage(Searchable searchable) {
        return umsOrgService.findAll(searchable);
    }

    @Override
    public Page<UmsUserOrgRelation> listUmsUserOrgRelationPage(Searchable searchable) {
        return umsUserOrgRelationService.findAll(searchable);
    }

    @Override
    public List<UmsOrg> listUmsOrg(Searchable searchable) {
        return umsOrgService.findAllWithSort(searchable);
    }

    @Override
    public List<UmsUserOrgRelation> listUmsUserOrgRelation(Searchable searchable) {
        return umsUserOrgRelationService.findAllWithSort(searchable);
    }

    @Override
    public UmsOrgRule saveUmsOrgRule(String orgId, String managerOrg) {
        return umsOrgService.saveUmsOrgRule(orgId, managerOrg);
    }

    @Override
    public UmsOrgRule updateUmsOrgRule(String orgId, String managerOrg) {
        return umsOrgService.saveUmsOrgRule(orgId, managerOrg);
    }

    @Override
    public void deleteUmsOrgRule(String... ids) {
        umsOrgService.deleteOrgRule(ids);
    }

    @Override
    public void deleteOrgRuleByOrg(String orgId) {
        umsOrgService.deleteOrgRuleByOrg(orgId);
    }

    @Override
    public List<String> listManagerRuleIds(String orgId) {
        return umsOrgService.listManagerRuleIds(orgId);
    }

    @Override
    public UmsOrgType createUmsOrgType(UmsOrgType umsOrgType) throws OrgTypeSnExistsException {
        return umsOrgTypeService.save(umsOrgType);
    }

    @Override
    public UmsOrgType updateUmsOrgType(UmsOrgType umsOrgType) {
        return umsOrgTypeService.update(umsOrgType);
    }

    @Override
    public void deleteUmsOrgType(String... ids) throws OrgTypeNotDeleteException {
        umsOrgTypeService.deleteOrgType(ids);
    }

    @Override
    public Page<UmsOrgType> listUmsOrgTypePage(Searchable searchable) {
        return umsOrgTypeService.findAll(searchable);
    }

    @Override
    public List<UmsOrgType> listUmsOrgType(Searchable searchable) {
        return umsOrgTypeService.findAllWithNoPageNoSort(searchable);
    }

    @Override
    public UmsOrgTypeRule createOrgTypeRule(UmsOrgTypeRule umsOrgTypeRule) {
        return umsOrgTypeService.saveOrgTypeRule(umsOrgTypeRule);
    }

    @Override
    public UmsOrgTypeRule updateOrgTypeRule(UmsOrgTypeRule umsOrgTypeRule) {
        return umsOrgTypeService.saveOrgTypeRule(umsOrgTypeRule);
    }

    @Override
    public void deleteOrgTypeRule(String... id) {
        umsOrgTypeService.deleteOrgTypeRule(id);
    }

    @Override
    public List<UmsOrgTypeRule> listOrgTypeRuleChild(String pid) {
        return umsOrgTypeService.listByRule(pid);
    }

    public UmsOrg updAvailable(String id, Short isAvailable) {
        UmsOrg org = umsOrgService.getOne(id);
        org.setIsAvailable(isAvailable);
        org = umsOrgService.update(org);
        return org;
    }

    @Override
    public UmsOrg getById(String orgId) {
        return umsOrgService.getOne(orgId);
    }

    @Override
    public Page<UmsUser> findUserByOrgId(String orgId, String nickname, String username, Pageable page) {
        return umsOrgService.findUserByOrgId(orgId, nickname, username, page);
    }

    @Override
    public Page<UmsUser> findNotUserByOrgId(String orgId, String nickname, String username, Pageable page) {
        return umsOrgService.findNotUserByOrgId(orgId, nickname, username, page);
    }

    @Override
    public void buildUserOrgRelation(String orgId, List<String> userIds) {
        umsOrgService.buildUserOrgRelation(orgId, userIds);
    }

    @Override
    public void clearUserOrgRelation(String orgId, List<String> userIds) {
        umsOrgService.clearUserOrgRelation(orgId, userIds);
    }

    @Override
    public Page<UmsUser> findUserByOrgIdAndNameAndSn(String orgId, String name, String sn, Pageable page) {
        return umsOrgService.findUserByOrgIdAndNameAndSn(orgId, name, sn, page);
    }

    @Override
    public Page<UmsUser> findNotUserByOrgIdAndNameAndSn(String orgId, String name, String sn, Pageable page) {
        return umsOrgService.findNotUserByOrgIdAndNameAndSn(orgId, name, sn, page);
    }

//	@Override
//	public void clearUserOrgPositionRelation(String orgId, String positionId, List<String> userIds) {
//		umsUserOrgGroupPosService.clearUserOrgPositionRelation(orgId, positionId, userIds);
//	}
//
//	@Override
//	public Page<UmsUser> findUserByOrgIdAndPositionId(String orgId, String positionId, String nickname, String username,
//			Pageable page) {
//		return umsUserOrgGroupPosService.findUserByOrgIdAndPositionId(orgId, positionId, nickname, username, page);
//	}
//
//	@Override
//	public Page<UmsUser> findNotUserByOrgIdAndPositionId(String orgId, String positionId, String nickname, String username,
//			Pageable page) {
//		return umsUserOrgGroupPosService.findNotUserByOrgIdAndPositionId(orgId, positionId, nickname, username, page);
//	}
//
//	@Override
//	public void buildUserOrgPositionRelation(String orgId, String positionId, List<String> userIds) {
//		umsUserOrgGroupPosService.buildUserOrgPositionRelation(orgId, positionId, userIds);
//	}
}
