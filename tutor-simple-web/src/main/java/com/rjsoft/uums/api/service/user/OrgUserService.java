package com.rjsoft.uums.api.service.user;

import com.rjsoft.common.vo.PageVO;
import com.rjsoft.uums.api.controller.vo.person.PersonVO;
import com.rjsoft.uums.api.controller.vo.user.OrgUserVO;

import java.util.List;

public interface OrgUserService {

	PageVO<OrgUserVO> getUser(String nickname, String username, String orgId, int number, int size, String currentUsername, List<String> currentUserRoleSn);

	PageVO<PersonVO> getPerson(String nickname, String username, String orgId, int number, int size, String currentUsername, List<String> currentUserRoleSn);
}
