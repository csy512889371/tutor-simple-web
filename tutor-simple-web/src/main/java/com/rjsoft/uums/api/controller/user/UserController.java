package com.rjsoft.uums.api.controller.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rjsoft.common.model.search.Searchable;
import com.rjsoft.common.vo.PageVO;
import com.rjsoft.common.vo.ViewerResult;
import com.rjsoft.uums.api.constant.FaySysRoleConstant;
import com.rjsoft.uums.api.constant.FayUserConstant;
import com.rjsoft.uums.api.controller.vo.user.DepartmentUserVO;
import com.rjsoft.uums.api.controller.vo.user.OrgUserVO;
import com.rjsoft.uums.api.controller.vo.user.PositionUserVO;
import com.rjsoft.uums.api.controller.vo.user.UserVO;
import com.rjsoft.uums.api.service.user.DepartmentUserService;
import com.rjsoft.uums.api.service.user.OrgUserService;
import com.rjsoft.uums.api.service.user.PositionUserService;
import com.rjsoft.uums.api.service.user.UserService;
import com.rjsoft.uums.facade.app.entity.UmsApp;
import com.rjsoft.uums.facade.app.service.UmsAppFacade;
import com.rjsoft.uums.facade.role.entity.UmsRole;
import com.rjsoft.uums.facade.role.service.UmsRoleFacade;
import com.rjsoft.uums.facade.user.entity.UmsPerson;
import com.rjsoft.uums.facade.user.entity.UmsUser;
import com.rjsoft.uums.facade.user.service.UmsUserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * user api
 *
 * @author feichongzheng
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UmsUserFacade umsUserFacade;

    @Resource
    private UmsAppFacade umsAppFacade;

    @Resource
    private UmsRoleFacade umsRoleFacade;

//	@Autowired
//	private RoleUserService roleUserService;

    @Autowired
    private UserService userService;

//	@Autowired
//	private OrgRoleUserService orgRoleUserService;

    @Autowired
    private OrgUserService orgUserService;

//	@Autowired
//	private PositionRoleUserService positionRoleUserService;

    @Autowired
    private PositionUserService positionUserService;

    @Autowired
    private DepartmentUserService departmentUserService;

//	@Autowired
//	private DepartmentRoleUserService departmentRoleUserService;

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/findForPage", method = RequestMethod.POST)
    public ViewerResult findForPage(HttpServletRequest request, @RequestBody JSONObject obj) {
        ViewerResult result = new ViewerResult();
        PageVO<UserVO> pageVO = null;
        PageVO<OrgUserVO> pageVOForOrg = null;
        PageVO<DepartmentUserVO> pageVOForDepartment = null;
        PageVO<PositionUserVO> pageVOForPosition = null;
//		PageVO<RoleUserVO> pageVOForRole = null;
        List<String> currentUserRoleSn = null;
        try {
            Object currentUsernameObj = request.getAttribute("currentUsername");
            Object object = request.getAttribute("currentUserRoleSn");
            if (object == null) {
                currentUserRoleSn = new ArrayList<>();
            } else {
                currentUserRoleSn = (List<String>) request.getAttribute("currentUserRoleSn");
            }
            if (currentUsernameObj == null) {
                pageVO = new PageVO<>(new ArrayList<>(), UserVO.class);
                result.setData(pageVO);
            } else {
                String currentUsername = (String) currentUsernameObj;

                String orgId = obj.getString("orgId");
                String departmentId = obj.getString("departmentId");
                String positionId = obj.getString("positionId");
//				String roleId = obj.getString("roleId");
                String nickname = obj.getString("nickname");
                String username = obj.getString("username");

                int number = obj.getInteger("number");
                int size = obj.getInteger("size");

                if (positionId == null) {
                    if (departmentId == null) {
                        if (orgId == null) {
//							if(roleId == null){
                            pageVO = userService.getUserByLoginUser(nickname, username, number, size, currentUsername, currentUserRoleSn);
                            result.setData(pageVO);
//							}else{
//								pageVOForRole = roleUserService.getUser(nickname, username, roleId, number, size, currentUsername, currentUserRoleSn);
//								result.setData(pageVOForRole);
//							}
                        } else {
//							if(roleId == null){
                            pageVOForOrg = orgUserService.getUser(nickname, username, orgId, number, size, currentUsername, currentUserRoleSn);
                            result.setData(pageVOForOrg);
//							}else{
//								pageVOForRole = orgRoleUserService.getUser(nickname, username, orgId, roleId, number, size, currentUsername, currentUserRoleSn);
//								result.setData(pageVOForRole);
//							}
                        }
                    } else {
//						if(roleId == null){
                        pageVOForDepartment = departmentUserService.getUser(nickname, username, departmentId, number, size, currentUsername, currentUserRoleSn);
                        result.setData(pageVOForDepartment);
//						}else{
//							pageVOForRole = departmentRoleUserService.getUser(nickname, username, departmentId, roleId, number, size, currentUsername, currentUserRoleSn);
//							result.setData(pageVOForRole);
//						}
                    }
                } else {
//					if(roleId == null){
                    pageVOForPosition = positionUserService.getUser(nickname, username, positionId, number, size, currentUsername, currentUserRoleSn);
                    result.setData(pageVOForPosition);
//					}else{
//						pageVOForRole = positionRoleUserService.getUser(nickname, username, positionId, roleId, number, size, currentUsername, currentUserRoleSn);
//						result.setData(pageVOForRole);
//					}
                }
            }
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/findById", method = RequestMethod.POST)
    public ViewerResult findById(@RequestBody JSONObject obj) {
        ViewerResult result = new ViewerResult();
        UmsUser user = null;
        try {
            String id = obj.getString("id");
            user = umsUserFacade.findById(id);
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

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ViewerResult add(@RequestBody UmsUser user) {
        ViewerResult result = new ViewerResult();
        try {
            user = umsUserFacade.register(user);

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

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ViewerResult update(@RequestBody UmsUser user) {
        ViewerResult result = new ViewerResult();
        UmsUser oldUser = null;
        try {
            oldUser = umsUserFacade.findById(user.getId());
            oldUser.setNickname(user.getNickname());
            user = umsUserFacade.update(oldUser);
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

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ViewerResult delete(@RequestBody JSONObject obj) {
        ViewerResult result = new ViewerResult();
        try {
            JSONArray ja = obj.getJSONArray("ids");
            String[] ids = ja.toJavaObject(String[].class);
            boolean flag = false;
            for (String username : FayUserConstant.SUPER_MANAGE_USERNAME) {
                UmsUser user = umsUserFacade.findByUsername(username);
                for (String id : ids) {
                    if (id.equals(user.getId())) {
                        flag = true;
                        result.setSuccess(false);
                        result.setErrMessage("用户名为：" + username + "的用户不可删除");
                        break;
                    }
                }
                if (flag) break;
            }
            if (!flag) {
                umsUserFacade.delete(ids);
                result.setSuccess(true);
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/updAvailable", method = RequestMethod.POST)
    public ViewerResult updAvailable(@RequestBody JSONObject obj) {
        ViewerResult result = new ViewerResult();
        try {
            String id = obj.getString("id");
            short isAvailable = new Short(obj.getString("isAvailable"));
            UmsUser user = umsUserFacade.updAvailable(id, isAvailable);
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

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public ViewerResult resetPassword(@RequestBody JSONObject obj) {
        ViewerResult result = new ViewerResult();
        UmsUser user = null;
        try {
            user = umsUserFacade.resetPassword(obj.getString("username"), FayUserConstant.DEFAULT_PASSWORD);
            user.setPerson(null);
            result.setSuccess(true);
            result.setData(user);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public ViewerResult updatePassword(@RequestBody JSONObject obj) {
        ViewerResult result = new ViewerResult();
        UmsPerson person = null;
        try {
//			person = umsUserFacade.updatePassword(obj.getString("username"), obj.getString("oldPassword"), obj.getString("newPassword"));
//			person.getUser().setPerson(null);
            result.setSuccess(true);
            result.setData(person);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
