package com.rjsoft.uums.facade.user.service.impl;

import com.rjsoft.uums.core.user.service.UmsPersonService;
import com.rjsoft.uums.facade.user.entity.UmsPerson;
import com.rjsoft.uums.facade.user.service.UmsPersonFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("umsPersonFacadeImpl")
public class UmsPersonFacadeImpl implements UmsPersonFacade {
	
	@Autowired
	private UmsPersonService umsPersonService;

	@Override
	public UmsPerson updatePerson(UmsPerson person) {
		return umsPersonService.updatePerson(person);
	}

	@Override
	public UmsPerson findById(String id) {
		return umsPersonService.getOne(id);
	}
}
