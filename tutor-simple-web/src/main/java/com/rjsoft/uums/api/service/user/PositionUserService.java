package com.rjsoft.uums.api.service.user;

import com.rjsoft.common.vo.PageVO;
import com.rjsoft.uums.api.controller.vo.person.PersonVO;
import com.rjsoft.uums.api.controller.vo.user.PositionUserVO;

import java.util.List;

public interface PositionUserService {

	PageVO<PositionUserVO> getUser(String nickname, String username, String positionId, int number, int size, String currentUsername, List<String> currentUserRoleSn);

	PageVO<PersonVO> getPerson(String name, String sn, String positionId, int number, int size, String currentUsername, List<String> currentUserRoleSn);
}
