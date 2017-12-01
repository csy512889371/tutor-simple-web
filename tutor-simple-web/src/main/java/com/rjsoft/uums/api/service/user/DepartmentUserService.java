package com.rjsoft.uums.api.service.user;

import com.rjsoft.common.vo.PageVO;
import com.rjsoft.uums.api.controller.vo.person.PersonVO;
import com.rjsoft.uums.api.controller.vo.user.DepartmentUserVO;

import java.util.List;

public interface DepartmentUserService {
	
	PageVO<DepartmentUserVO> getUser(String nickname, String username, String departmentId, int number, int size, String currentUsername, List<String> currentUserRoleSn);

	PageVO<PersonVO> getPerson(String name, String sn, String departmentId, int number, int size, String currentUsername, List<String> currentUserRoleSn);
}
