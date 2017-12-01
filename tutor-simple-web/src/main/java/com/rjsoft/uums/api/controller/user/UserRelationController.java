package com.rjsoft.uums.api.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.rjsoft.common.model.search.Searchable;
import com.rjsoft.common.vo.ViewerResult;
import com.rjsoft.uums.api.constant.FaySysRoleConstant;
import com.rjsoft.uums.api.controller.vo.user.UserVO;
import com.rjsoft.uums.facade.app.entity.UmsApp;
import com.rjsoft.uums.facade.app.service.UmsAppFacade;
import com.rjsoft.uums.facade.department.service.UmsDepartmentFacade;
import com.rjsoft.uums.facade.org.service.UmsOrgFacade;
import com.rjsoft.uums.facade.orgRole.service.UmsOrgRoleFacade;
import com.rjsoft.uums.facade.position.service.UmsPositionFacade;
import com.rjsoft.uums.facade.role.entity.UmsRole;
import com.rjsoft.uums.facade.role.service.UmsRoleFacade;
import com.rjsoft.uums.facade.user.entity.UmsPerson;
import com.rjsoft.uums.facade.user.entity.UmsUser;
import com.rjsoft.uums.facade.user.service.UmsUserFacade;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * user api
 *
 * @author feichongzheng
 */
@RestController
@RequestMapping("/api/user/relation")
public class UserRelationController {

    @Resource
    private UmsUserFacade umsUserFacade;

    @Resource
    private UmsAppFacade umsAppFacade;

    @Resource
    private UmsRoleFacade umsRoleFacade;

    @Resource
    private UmsOrgFacade umsOrgFacade;

    @Resource
    private UmsDepartmentFacade umsDepartmentFacade;

    @Resource
    private UmsPositionFacade umsPositionFacade;

    @Resource
    private UmsOrgRoleFacade umsOrgRoleFacade;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ViewerResult add(@RequestBody JSONObject obj) {
        ViewerResult result = new ViewerResult();
        UmsUser user = new UmsUser();
        UmsPerson person = new UmsPerson();
        try {
            String name = obj.getString("name");
            String sn = obj.getString("sn");
            String password = obj.getString("password");
            user.setNickname(name);
            user.setUsername(sn);
            user.setPassword(password);
            person.setName(name);
            person.setSn(sn);
            user.setPerson(person);
            user = umsUserFacade.register(user);

            String orgId = obj.getString("orgId");
            String departmentId = obj.getString("departmentId");
            String positionId = obj.getString("positionId");
            String roleId = obj.getString("roleId");
            String orgRoleId = obj.getString("orgRoleId");
            List<String> userIds = new ArrayList<>();
            userIds.add(user.getId());

            if (orgId != null) {
                umsOrgFacade.buildUserOrgRelation(orgId, userIds);
            }
            if (departmentId != null) {
                umsDepartmentFacade.buildUserDepartmentRelation(departmentId, userIds);
            }
            if (positionId != null) {
                umsPositionFacade.buildUserPositionRelation(positionId, userIds);
            }
            if (roleId != null) {
                umsRoleFacade.buildUserRoleRelation(roleId, userIds.toArray(new String[userIds.size()]));
            }
            if (orgRoleId != null) {
                umsOrgRoleFacade.buildUserRoleRelation(orgRoleId, userIds.toArray(new String[userIds.size()]));
            }

            Searchable searchable = Searchable.newSearchable();
            List<UmsApp> apps = umsAppFacade.list(searchable);
            for (UmsApp app : apps) {
                UmsRole role = umsRoleFacade.getBySn(FaySysRoleConstant.GENERAL_ROLE_SN_PREFIX + app.getSn());
                if (role != null) {
                    umsRoleFacade.buildUserRoleRelation(role.getId(), new String[]{user.getId()});
                }
            }

            UserVO userVO = new UserVO();
            userVO.convertPOToVO(user);
            result.setSuccess(true);
            result.setData(userVO);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public ViewerResult remove(@RequestBody JSONObject obj) {
        ViewerResult result = new ViewerResult();
        try {
            String userId = obj.getString("userId");
            String orgId = obj.getString("orgId");
            String departmentId = obj.getString("departmentId");
            String positionId = obj.getString("positionId");
            String roleId = obj.getString("roleId");
            List<String> userIds = new ArrayList<>();
            userIds.add(userId);

            if (orgId != null) {
                umsOrgFacade.clearUserOrgRelation(orgId, userIds);
            }
            if (departmentId != null) {
                umsDepartmentFacade.clearUserDepartmentRelation(departmentId, userIds);
            }
            if (positionId != null) {
                umsPositionFacade.clearUserPositionRelation(positionId, userIds);
            }
            if (roleId != null) {
                umsRoleFacade.clearUserRoleRelation(roleId, userIds);
            }
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
