package com.rjsoft.uums.api.service.user;

import com.rjsoft.common.vo.PageVO;
import com.rjsoft.uums.api.controller.vo.person.PersonVO;
import com.rjsoft.uums.api.controller.vo.user.UserVO;

import java.util.List;

public interface UserService {
	
	List<String> getOrgIdsForManageByLoginUser(String username);
	
	List<String> getUserIdsForManageByLoginUser(String username);

	PageVO<UserVO> getUserByLoginUser(String nickname, String username, int number, int size, String currentUsername, List<String> currentUserRoleSn);
	
	PageVO<PersonVO> getPersonByLoginUser(String name, String sn, int number, int size, String currentUsername, List<String> currentUserRoleSn);
	
	List<String> getUserIdsByOrg(String nickname, String username, String orgId);
	
	List<String> getUserIdsByDepartment(String nickname, String username, String departmentId);
	
	List<String> getUserIdsByPosition(String nickname, String username, String positionId);
}
