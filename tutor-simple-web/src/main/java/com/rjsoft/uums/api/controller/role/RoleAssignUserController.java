package com.rjsoft.uums.api.controller.role;

import com.alibaba.fastjson.JSONObject;
import com.rjsoft.common.vo.PageVO;
import com.rjsoft.common.vo.ViewerResult;
import com.rjsoft.uums.api.controller.vo.user.AssignUserVO;
import com.rjsoft.uums.facade.role.service.UmsRoleFacade;
import com.rjsoft.uums.facade.user.entity.UmsUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/role")
public class RoleAssignUserController {

    @Resource
    private UmsRoleFacade umsRoleFacade;

    /**
     * get all assigned users by conditions
     *
     * @return
     */
    @RequestMapping(value = "/findAssignedUsers", method = RequestMethod.POST)
    public ViewerResult findUsersForAssign(@RequestBody JSONObject obj) {
        ViewerResult result = new ViewerResult();
        Page<UmsUser> pageUser = null;
        PageVO<AssignUserVO> pageVO = null;
        try {
            //get JSON format parameters
            String roleId = obj.getString("dataId");
            String nickname = obj.getString("nickname");
            String username = obj.getString("username");
            int number = Integer.valueOf(obj.getString("number"));
            int size = Integer.valueOf(obj.getString("size"));

//			Sort sort = new Sort(Direction.DESC, "createDate");

            Pageable page = PageRequest.of(number, size);

            pageUser = umsRoleFacade.findUserByRoleId(roleId, nickname, username, page);
            //convert to PageVO for view
            pageVO = new PageVO<>(pageUser, AssignUserVO.class);
            result.setSuccess(true);
            result.setData(pageVO);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * get all unassigned users by conditions
     *
     * @return
     */
    @RequestMapping(value = "/findUnassignedUsers", method = RequestMethod.POST)
    public ViewerResult findUnassignedUsers(@RequestBody JSONObject obj) {
        ViewerResult result = new ViewerResult();
        Page<UmsUser> pageUser = null;
        PageVO<AssignUserVO> pageVO = null;
        try {
            //get JSON format parameters
            String roleId = obj.getString("dataId");
            String nickname = obj.getString("nickname");
            String username = obj.getString("username");
            int number = Integer.valueOf(obj.getString("number"));
            int size = Integer.valueOf(obj.getString("size"));

//			Sort sort = new Sort(Direction.DESC, "createDate");

            Pageable page = PageRequest.of(number, size);

            pageUser = umsRoleFacade.findNotUserByRoleId(roleId, nickname, username, page);
            //convert to PageVO for view
            pageVO = new PageVO<>(pageUser, AssignUserVO.class);
            result.setSuccess(true);
            result.setData(pageVO);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/assignUser", method = RequestMethod.POST)
    public ViewerResult assignUser(@RequestBody JSONObject obj) {
        ViewerResult result = new ViewerResult();
        try {
            //get JSON format parameters
            String roleId = obj.getString("dataId");
            List<String> userIds = (List<String>) obj.get("userIds");
            if (userIds != null && userIds.size() > 0) {
                String[] userIdArr = new String[userIds.size()];
                userIds.toArray(userIdArr);
                umsRoleFacade.buildUserRoleRelation(roleId, userIdArr);
            }
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage("分配用户失败");
            e.printStackTrace();
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/unassignUser", method = RequestMethod.POST)
    public ViewerResult unassignUser(@RequestBody JSONObject obj) {
        ViewerResult result = new ViewerResult();
        try {
            //get JSON format parameters
            String roleId = obj.getString("dataId");
            List<String> userIds = (List<String>) obj.get("userIds");
            umsRoleFacade.clearUserRoleRelation(roleId, userIds);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage("去掉分配用户失败");
            e.printStackTrace();
        }
        return result;
    }
}
