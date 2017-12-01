package com.rjsoft.uums.facade.position.service.impl;

import com.rjsoft.common.model.search.Searchable;
import com.rjsoft.uums.core.department.service.UmsDepartmentService;
import com.rjsoft.uums.core.org.service.UmsOrgService;
import com.rjsoft.uums.core.position.service.UmsPositionService;
import com.rjsoft.uums.core.position.service.UmsUserPositionRelationService;
import com.rjsoft.uums.facade.department.entity.UmsDepartment;
import com.rjsoft.uums.facade.org.entity.UmsOrg;
import com.rjsoft.uums.facade.position.entity.UmsPosition;
import com.rjsoft.uums.facade.position.entity.UmsUserPositionRelation;
import com.rjsoft.uums.facade.position.exception.PositionException;
import com.rjsoft.uums.facade.position.exception.PositionSnExistsException;
import com.rjsoft.uums.facade.position.service.UmsPositionFacade;
import com.rjsoft.uums.facade.user.entity.UmsUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("umsPositionFacadeImpl")
public class UmsPositionFacadeImpl implements UmsPositionFacade {
	
	@Autowired
	private UmsPositionService umsPositionService;
	
	@Autowired
	private UmsOrgService umsOrgService;
	
	@Autowired
	private UmsDepartmentService umsDepartmentService;
	
	@Autowired
	private UmsUserPositionRelationService umsUserPositionRelationService;

	@Override
	public UmsPosition create(UmsPosition umsPosition) throws PositionSnExistsException {
		String orgId = umsPosition.getOrgId();
		String departmentId = umsPosition.getDepartmentId();
		if(orgId != null) {
			UmsOrg org = umsOrgService.getOne(orgId);
			if(org == null){
				throw new PositionException("position.org.not.exited", null);
			}
		}
		if(departmentId != null) {
			UmsDepartment department = umsDepartmentService.getOne(departmentId);
			if (department == null) {
				throw new PositionException("position.department.not.exited", null);
			}
			if (orgId == null) {
				umsPosition.setOrgId(department.getOrgId());
			}
		}
		return umsPositionService.savePosition(umsPosition);
	}

	@Override
	public UmsPosition update(UmsPosition umsPosition) {
		return umsPositionService.update(umsPosition);
	}

	@Override
	public void delete(String... ids) {
		umsPositionService.deletePosition(ids);
	}

	@Override
	public UmsPosition getBySn(String sn) {
		return umsPositionService.findBySn(sn);
	}

	@Override
	public UmsPosition getById(String id) {
		return umsPositionService.getOne(id);
	}

	@Override
	public Page<UmsPosition> listPage(Searchable searchable) {
		return umsPositionService.findAll(searchable);
	}

	@Override
	public List<UmsPosition> list(Searchable searchable) {
		return umsPositionService.findAllWithNoPageNoSort(searchable);
	}

	public UmsPosition updAvailable(String id, Short isAvailable) {
		UmsPosition position = umsPositionService.getOne(id);
		position.setIsAvailable(isAvailable);
		position = umsPositionService.update(position);
		return position;
	}
	
	@Override
	public Page<UmsUser> findUserByPositionId(String positionId, String nickname, String username, Pageable page) {
		return umsPositionService.findUserByPositionId(positionId, nickname, username, page);
	}

	@Override
	public Page<UmsUser> findNotUserByPositionId(String positionId, String nickname, String username, Pageable page) {
		return umsPositionService.findNotUserByPositionId(positionId, nickname, username, page);
	}
	
	@Override
	public void buildUserPositionRelation(String positionId, List<String> userIds) {
		umsPositionService.buildUserPositionRelation(positionId, userIds);
	}
	
	@Override
	public void clearUserPositionRelation(String positionId, List<String> userIds){
		umsPositionService.clearUserPositionRelation(positionId, userIds);
	}

	@Override
	public Page<UmsUserPositionRelation> listUmsUserPositionRelationPage(Searchable searchable) {
		return umsUserPositionRelationService.findAll(searchable);
	}

	@Override
	public List<UmsUserPositionRelation> listUmsUserPositionRelation(Searchable searchable) {
		return umsUserPositionRelationService.findAllWithSort(searchable);
	}

	@Override
	public Page<UmsUser> findUserByPositionIdAndNameAndSn(String positionId, String name, String sn, Pageable page) {
		return umsPositionService.findUserByPositionIdAndNameAndSn(positionId, name, sn, page);
	}

	@Override
	public Page<UmsUser> findNotUserByPositionIdAndNameAndSn(String positionId, String name, String sn, Pageable page) {
		return umsPositionService.findNotUserByPositionIdAndNameAndSn(positionId, name, sn, page);
	}
}
