package com.rjsoft.uums.api.controller.resource;

import com.alibaba.fastjson.JSONObject;
import com.rjsoft.common.entity.enums.AvailableEnum;
import com.rjsoft.common.model.search.Searchable;
import com.rjsoft.common.vo.ListVO;
import com.rjsoft.common.vo.ViewerResult;
import com.rjsoft.uums.api.constant.FaySysRoleConstant;
import com.rjsoft.uums.api.constant.FayUserConstant;
import com.rjsoft.uums.api.controller.vo.resource.ControllerResourceForNavVO;
import com.rjsoft.uums.api.service.role.RoleService;
import com.rjsoft.uums.facade.auth.service.UmsAuthFacade;
import com.rjsoft.uums.facade.resource.entity.UmsControllerResources;
import com.rjsoft.uums.facade.resource.service.UmsControllerResFacade;
import com.rjsoft.uums.facade.user.entity.UmsUser;
import com.rjsoft.uums.facade.user.service.UmsUserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/menuResource/auth")
public class ControllerResourceAuthController {

    @Resource
    private UmsAuthFacade umsAuthFacade;

    @Resource
    private UmsUserFacade umsUserFacade;

    @Resource
    private UmsControllerResFacade umsControllerResFacade;

    @Autowired
    private RoleService roleService;

    /**
     * get all authorized menu by conditions
     *
     * @return
     */
    @RequestMapping(value = "/findRequestUrls", method = RequestMethod.POST)
    public ViewerResult findMenu(HttpServletRequest request, @RequestBody JSONObject obj) {
        ViewerResult result = new ViewerResult();
        Set<UmsControllerResources> umsControllerResources = null;
        List<UmsControllerResources> umsControllerResourceList = null;
        ListVO<ControllerResourceForNavVO> listVO = null;
        try {
            Object currentUsername = request.getAttribute("currentUsername");
            if (currentUsername == null) {
                umsControllerResources = new HashSet<>();
                listVO = new ListVO<>(umsControllerResources, ControllerResourceForNavVO.class);
            } else {
                String username = (String) currentUsername;
                String appSn = (String) request.getAttribute("currentAppSn");
                if (Arrays.asList(FayUserConstant.SUPER_MANAGE_USERNAME).contains(username) || roleService.validate(username, FaySysRoleConstant.SUPER_MANAGE_ROLE_SN)) {
                    Searchable searchable = Searchable.newSearchable();
                    searchable.addSearchParam("isAvailable_eq", AvailableEnum.TRUE.getValue());
                    searchable.addSearchParam("application.sn_eq", appSn);
                    umsControllerResourceList = umsControllerResFacade.listUmsControllerRes(searchable);
                    listVO = new ListVO<>(umsControllerResourceList, ControllerResourceForNavVO.class);
                } else {
                    UmsUser user = umsUserFacade.findByUsername(username);
                    umsControllerResources = umsControllerResFacade.getControllerResByUserAndAppSn(user, appSn, null, AvailableEnum.TRUE.getValue());
                    listVO = new ListVO<>(umsControllerResources, ControllerResourceForNavVO.class);
                }
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
