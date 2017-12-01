package com.rjsoft.uums.facade.user.service;

import com.rjsoft.common.model.search.Searchable;
import com.rjsoft.uums.facade.user.entity.UmsUser;
import com.rjsoft.uums.facade.user.exception.*;
import org.springframework.data.domain.Page;

/**
 * User:cxtww
 * Date:2016年11月23日 下午1:46:09
 * Version:1.0
 */
public interface UmsUserFacade {
	
	UmsUser findByUsername(String username);
	
	UmsUser login(String username, String password) throws UserNotExistsException,UserUnAvailableException,UserPasswordNotMatchException;
	
	UmsUser register(UmsUser user) throws UserUsernameEmptyException,UserPasswordEmptyException,UserNicknameEmptyException,UserUsernameNotValidException,UserPasswordNotValidException,UserNicknameNotValidException,UserUsernameExistsException;
	
	UmsUser resetPassword(String username, String newPassword);
	
	UmsUser updatePassword(String username, String oldPassword, String newPassword);
	
	UmsUser update(UmsUser user);
	
	/**
	 * 分页查询
	 * @param searchable
	 * @return
	 */
	public Page<UmsUser> listPage(Searchable searchable);
	
	/**
	 * 修改是否可用
	 * @param id
	 * @param isAvailable
	 * @return
	 */
	public UmsUser updAvailable(String id, Short isAvailable);
	
	UmsUser findById(String id);
	
	void delete(String... ids);
}
