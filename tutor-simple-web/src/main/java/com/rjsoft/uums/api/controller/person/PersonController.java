package com.rjsoft.uums.api.controller.person;

import com.alibaba.fastjson.JSONObject;
import com.rjsoft.common.model.search.Searchable;
import com.rjsoft.common.vo.PageVO;
import com.rjsoft.common.vo.ViewerResult;
import com.rjsoft.uums.api.constant.FaySysRoleConstant;
import com.rjsoft.uums.api.constant.FayUserConstant;
import com.rjsoft.uums.api.controller.vo.person.PersonVO;
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
import com.rjsoft.uums.facade.user.service.UmsPersonFacade;
import com.rjsoft.uums.facade.user.service.UmsUserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * person api
 *
 * @author feichongzheng
 */
@RestController
@RequestMapping("/api/person")
public class PersonController {

    @Resource
    private UmsPersonFacade umsPersonFacade;

    @Resource
    private UmsUserFacade umsUserFacade;

    @Resource
    private UmsAppFacade umsAppFacade;

    @Resource
    private UmsRoleFacade umsRoleFacade;

    @Autowired
    private UserService userService;

    @Autowired
    private OrgUserService orgUserService;

    @Autowired
    private PositionUserService positionUserService;

    @Autowired
    private DepartmentUserService departmentUserService;

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/findForPage", method = RequestMethod.POST)
    public ViewerResult findForPage(HttpServletRequest request, @RequestBody JSONObject obj) {
        ViewerResult result = new ViewerResult();
        PageVO<PersonVO> pageVO = null;
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
                pageVO = new PageVO<>(new ArrayList<>(), PersonVO.class);
                result.setData(pageVO);
            } else {
                String currentUsername = (String) currentUsernameObj;

                String orgId = obj.getString("orgId");
                String departmentId = obj.getString("departmentId");
                String positionId = obj.getString("positionId");
//				String roleId = obj.getString("roleId");
                String name = obj.getString("name");
                String sn = obj.getString("sn");

                int number = obj.getInteger("number");
                int size = obj.getInteger("size");

                if (positionId == null) {
                    if (departmentId == null) {
                        if (orgId == null) {
//							if(roleId == null){
                            pageVO = userService.getPersonByLoginUser(name, sn, number, size, currentUsername, currentUserRoleSn);
                            result.setData(pageVO);
//							}else{
//								pageVOForRole = roleUserService.getUser(nickname, username, roleId, number, size, currentUsername, currentUserRoleSn);
//								result.setData(pageVOForRole);
//							}
                        } else {
//							if(roleId == null){
                            pageVO = orgUserService.getPerson(name, sn, orgId, number, size, currentUsername, currentUserRoleSn);
                            result.setData(pageVO);
//							}else{
//								pageVOForRole = orgRoleUserService.getUser(nickname, username, orgId, roleId, number, size, currentUsername, currentUserRoleSn);
//								result.setData(pageVOForRole);
//							}
                        }
                    } else {
//						if(roleId == null){
                        pageVO = departmentUserService.getPerson(name, sn, departmentId, number, size, currentUsername, currentUserRoleSn);
                        result.setData(pageVO);
//						}else{
//							pageVOForRole = departmentRoleUserService.getUser(nickname, username, departmentId, roleId, number, size, currentUsername, currentUserRoleSn);
//							result.setData(pageVOForRole);
//						}
                    }
                } else {
//					if(roleId == null){
                    pageVO = positionUserService.getPerson(name, sn, positionId, number, size, currentUsername, currentUserRoleSn);
                    result.setData(pageVO);
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
        UmsPerson person = null;
        try {
            String id = obj.getString("id");
            person = umsPersonFacade.findById(id);
            PersonVO personVO = new PersonVO();
            personVO.convertPOToVO(person);
            result.setSuccess(true);
            result.setData(personVO);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ViewerResult add(@RequestBody UmsPerson person) {
        ViewerResult result = new ViewerResult();
        try {
            UmsUser user = person.getUser();
            if (user == null) user = new UmsUser();
            if (user.getNickname() == null) user.setNickname(person.getName());
            if (user.getUsername() == null) user.setUsername(person.getSn());
            if (user.getPassword() == null) user.setPassword(FayUserConstant.DEFAULT_PASSWORD);
            user.setPerson(person);
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
            UmsPerson person = oldUser.getPerson();
            person.setName(user.getPerson().getName());
            person = umsPersonFacade.updatePerson(person);
            oldUser.setPerson(person);
            UserVO userVO = new UserVO();
            userVO.convertPOToVO(oldUser);
            result.setSuccess(true);
            result.setData(userVO);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
