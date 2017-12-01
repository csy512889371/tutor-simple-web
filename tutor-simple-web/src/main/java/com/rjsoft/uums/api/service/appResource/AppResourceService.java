package com.rjsoft.uums.api.service.appResource;

import java.util.List;

public interface AppResourceService {

	Object findAppResourceInTreeByLoginUser(String name, String currentUsername, List<String> currentUserRoleSn);
}
