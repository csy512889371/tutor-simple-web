package com.rjsoft.uums.api.service.orgDepartmentPositionUser;

import java.util.List;

public interface OrgDepartmentPositionUserService {

	Object findOrgDepartmentPositionUserInTreeByLoginUser(String name, String currentUsername, List<String> currentUserRoleSn);
}
