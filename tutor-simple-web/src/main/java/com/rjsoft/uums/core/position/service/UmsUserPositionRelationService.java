package com.rjsoft.uums.core.position.service;

import com.rjsoft.common.service.BaseService;
import com.rjsoft.uums.core.position.repository.UmsUserPositionRelationRepository;
import com.rjsoft.uums.facade.position.entity.UmsUserPositionRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * service接口
 * Date:2016-11-09 21:33
 * User:chenxiang
 * Version:1.0
 *
 */
@Service
public class UmsUserPositionRelationService extends BaseService<UmsUserPositionRelation,String> {
	
	@Autowired
	private UmsUserPositionRelationRepository umsUserPositionRelationRepository;
	
	public void deleteByUserId(String userId){
		umsUserPositionRelationRepository.clearUserPositionRelationByUserId(userId);
	}
}