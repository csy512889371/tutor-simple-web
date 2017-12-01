package com.rjsoft.uums.facade.user.service;

import com.rjsoft.uums.facade.user.entity.UmsPerson;

/**
 * 
 * @author feichongzheng
 *
 */
public interface UmsPersonFacade {
	
	UmsPerson updatePerson(UmsPerson person);
	
	UmsPerson findById(String id);
}
