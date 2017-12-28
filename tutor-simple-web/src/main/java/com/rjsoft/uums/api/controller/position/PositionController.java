package com.rjsoft.uums.api.controller.position;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rjsoft.common.entity.enums.AvailableEnum;
import com.rjsoft.common.model.search.Searchable;
import com.rjsoft.common.vo.ListVO;
import com.rjsoft.common.vo.PageVO;
import com.rjsoft.common.vo.ViewerResult;
import com.rjsoft.uums.api.constant.FayOrgRoleConstant;
import com.rjsoft.uums.api.constant.FaySysRoleConstant;
import com.rjsoft.uums.api.constant.FayUserConstant;
import com.rjsoft.uums.api.controller.vo.position.PositionForSelectVO;
import com.rjsoft.uums.api.controller.vo.position.PositionVO;
import com.rjsoft.uums.api.service.orgRole.OrgRoleService;
import com.rjsoft.uums.api.service.role.RoleService;
import com.rjsoft.uums.api.service.user.UserService;
import com.rjsoft.uums.facade.department.entity.UmsDepartment;
import com.rjsoft.uums.facade.department.service.UmsDepartmentFacade;
import com.rjsoft.uums.facade.org.entity.UmsOrg;
import com.rjsoft.uums.facade.org.service.UmsOrgFacade;
import com.rjsoft.uums.facade.position.entity.UmsPosition;
import com.rjsoft.uums.facade.position.entity.UmsUserPositionRelation;
import com.rjsoft.uums.facade.position.service.UmsPositionFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/position")
public class PositionController {

    @Resource
    private UmsOrgFacade umsOrgFacade;

    @Resource
    private UmsDepartmentFacade umsDepartmentFacade;

    @Resource
    private UmsPositionFacade umsPositionFacade;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private OrgRoleService orgRoleService;

    /**
     * get all positions by conditions for page
     *
     * @param name
     * @param number
     * @param size
     * @return
     */
    @RequestMapping(value = "/findForPage", method = RequestMethod.POST)
    public ViewerResult findForPage(HttpServletRequest request, @RequestBody JSONObject obj) {
        ViewerResult result = new ViewerResult();
        Page<UmsPosition> pagePosition = null;
        Page<UmsUserPositionRelation> uuprs = null;
        PageVO<PositionVO> pageVO = null;
        try {
            Object currentUsername = request.getAttribute("currentUsername");
            if (currentUsername != null) {
                String username = (String) currentUsername;
                String name = obj.getString("name");
                String orgId = obj.getString("orgId");
                String departmentId = obj.getString("departmentId");
                int number = obj.getInteger("number");
                int size = obj.getInteger("size");
                Pageable page = PageRequest.of(number, size);
                Searchable searchable = Searchable.newSearchable();
                searchable.setPage(page);
                if (Arrays.asList(FayUserConstant.SUPER_MANAGE_USERNAME).contains(username) || roleService.validate(username, FaySysRoleConstant.SUPER_MANAGE_ROLE_SN)) {
                    searchable.addSearchParam("name_like", name);
                    searchable.addSearchParam("orgId_eq", orgId);
                    searchable.addSearchParam("departmentId_eq", departmentId);
                    pagePosition = umsPositionFacade.listPage(searchable);
                    pageVO = new PageVO<>(pagePosition, PositionVO.class);
                } else if (orgRoleService.validate(username, FayOrgRoleConstant.ORG_SUPER_MANAGE_ROLE_SN)) {
                    searchable.addSearchParam("name_like", name);
                    searchable.addSearchParam("departmentId_eq", departmentId);
                    List<String> orgIds = userService.getOrgIdsForManageByLoginUser(username);
                    if (orgId == null) {
                        searchable.addSearchParam("orgId_in", orgIds);
                        pagePosition = umsPositionFacade.listPage(searchable);
                        pageVO = new PageVO<>(pagePosition, PositionVO.class);
                    } else if (orgIds.contains(orgId)) {
                        searchable.addSearchParam("orgId_eq", orgId);
                        pagePosition = umsPositionFacade.listPage(searchable);
                        pageVO = new PageVO<>(pagePosition, PositionVO.class);
                    } else {
                        pageVO = new PageVO<>(new ArrayList<>(), PositionVO.class);
                    }
                } else {
                    searchable.addSearchParam("position.name_like", name);
                    searchable.addSearchParam("position.orgId_eq", orgId);
                    searchable.addSearchParam("position.departmentId_eq", departmentId);
                    searchable.addSearchParam("user.username_eq", username);
                    uuprs = umsPositionFacade.listUmsUserPositionRelationPage(searchable);
                    pageVO = new PageVO<>(uuprs, PositionVO.class);
                }
                for (PositionVO vo : pageVO.getPageData()) {
                    String oId = vo.getOrgId();
                    if (oId != null) {
                        UmsOrg org = umsOrgFacade.getById(oId);
                        if (org != null) vo.setOrgName(org.getName());
                    }
                    String gId = vo.getDepartmentId();
                    if (gId != null) {
                        UmsDepartment department = umsDepartmentFacade.getById(vo.getDepartmentId());
                        if (department != null) vo.setDepartmentName(department.getName());
                    }
                }
            } else {
                pageVO = new PageVO<>(new ArrayList<>(), PositionVO.class);
            }
            result.setSuccess(true);
            result.setData(pageVO);
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
        UmsPosition position = null;
        try {
            String id = obj.getString("id");
            position = umsPositionFacade.getById(id);
            PositionVO positionVO = new PositionVO();
            positionVO.convertPOToVO(position);
            result.setSuccess(true);
            result.setData(positionVO);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ViewerResult add(@RequestBody UmsPosition position) {
        ViewerResult result = new ViewerResult();
        try {
            position = umsPositionFacade.create(position);
            PositionVO positionVO = new PositionVO();
            positionVO.convertPOToVO(position);
            result.setSuccess(true);
            result.setData(positionVO);
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
            umsPositionFacade.delete(ids);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ViewerResult update(@RequestBody UmsPosition position) {
        ViewerResult result = new ViewerResult();
        UmsPosition currentPosition = null;
        try {
            currentPosition = umsPositionFacade.getById(position.getId());
            currentPosition.setName(position.getName());
            currentPosition.setSn(position.getSn());
            currentPosition.setOrgId(position.getOrgId());
            currentPosition.setDepartmentId(position.getDepartmentId());
            position = umsPositionFacade.update(currentPosition);
            PositionVO positionVO = new PositionVO();
            positionVO.convertPOToVO(position);
            result.setSuccess(true);
            result.setData(positionVO);
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
            UmsPosition position = umsPositionFacade.updAvailable(id, isAvailable);
            PositionVO positionVO = new PositionVO();
            positionVO.convertPOToVO(position);
            result.setSuccess(true);
            result.setData(positionVO);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/findForSelect", method = RequestMethod.POST)
    public ViewerResult findForSelect(HttpServletRequest request, @RequestBody JSONObject obj) {
        ViewerResult result = new ViewerResult();
        List<UmsPosition> orgList = null;
        List<UmsUserPositionRelation> userPositionRelationList = null;
        ListVO<PositionForSelectVO> listVO = null;
        try {
            Object currentUsername = request.getAttribute("currentUsername");
            if (currentUsername != null) {
                String username = (String) currentUsername;
                String orgId = obj.getString("orgId");
                String departmentId = obj.getString("departmentId");
                Searchable searchable = Searchable.newSearchable();
                if (Arrays.asList(FayUserConstant.SUPER_MANAGE_USERNAME).contains(username) || roleService.validate(username, FaySysRoleConstant.SUPER_MANAGE_ROLE_SN) || roleService.validate(username, FaySysRoleConstant.MANAGE_ROLE_SN)) {
                    searchable.addSearchParam("orgId_eq", orgId);
                    searchable.addSearchParam("departmentId_eq", departmentId);
                    searchable.addSearchParam("isAvailable_eq", AvailableEnum.TRUE.getValue());
                    orgList = umsPositionFacade.list(searchable);
                    listVO = new ListVO<>(orgList, PositionForSelectVO.class);
                } else if (orgRoleService.validate(username, FayOrgRoleConstant.ORG_SUPER_MANAGE_ROLE_SN)) {
                    List<String> orgIds = userService.getOrgIdsForManageByLoginUser(username);
                    if (orgId == null) {
                        searchable.addSearchParam("orgId_in", orgIds);
                        searchable.addSearchParam("departmentId_eq", departmentId);
                        searchable.addSearchParam("isAvailable_eq", AvailableEnum.TRUE.getValue());
                        orgList = umsPositionFacade.list(searchable);
                    } else if (orgIds.contains(orgId)) {
                        searchable.addSearchParam("orgId_eq", orgId);
                        searchable.addSearchParam("departmentId_eq", departmentId);
                        searchable.addSearchParam("isAvailable_eq", AvailableEnum.TRUE.getValue());
                        orgList = umsPositionFacade.list(searchable);
                    } else {
                        orgList = new ArrayList<>();
                    }
                    listVO = new ListVO<>(orgList, PositionForSelectVO.class);
                } else {
                    searchable.addSearchParam("position.orgId_eq", orgId);
                    searchable.addSearchParam("position.departmentId_eq", departmentId);
                    searchable.addSearchParam("position.isAvailable_eq", AvailableEnum.TRUE.getValue());
                    searchable.addSearchParam("user.username_eq", username);
                    userPositionRelationList = umsPositionFacade.listUmsUserPositionRelation(searchable);
                    listVO = new ListVO<>(userPositionRelationList, PositionForSelectVO.class);
                }
            } else {
                listVO = new ListVO<>(orgList, PositionForSelectVO.class);
            }
            result.setSuccess(true);
            result.setData(listVO);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
