package com.rjsoft.uums.api.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.rjsoft.common.entity.enums.AvailableEnum;
import com.rjsoft.common.model.search.Searchable;
import com.rjsoft.common.vo.ListVO;
import com.rjsoft.common.vo.ViewerResult;
import com.rjsoft.uums.api.constant.FaySysRoleConstant;
import com.rjsoft.uums.api.constant.FayUserConstant;
import com.rjsoft.uums.api.controller.vo.common.AppForSelectVO;
import com.rjsoft.uums.api.service.role.RoleService;
import com.rjsoft.uums.facade.app.entity.UmsApp;
import com.rjsoft.uums.facade.app.service.UmsAppFacade;
import com.rjsoft.uums.facade.resource.entity.UmsControllerResources;
import com.rjsoft.uums.facade.resource.entity.UmsMenuResources;
import com.rjsoft.uums.facade.resource.service.UmsControllerResFacade;
import com.rjsoft.uums.facade.resource.service.UmsMenuResFacade;
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
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/app/auth")
public class AppAuthController {

    @Resource
    private UmsAppFacade umsAppFacade;

    @Resource
    private UmsUserFacade umsUserFacade;

    @Resource
    private UmsMenuResFacade umsMenuResFacade;

    @Resource
    private UmsControllerResFacade umsControllerResFacade;

    @Autowired
    private RoleService roleService;

    /**
     * get all app by conditions for select
     *
     * @return
     */
    @RequestMapping(value = "/findAppForSelect", method = RequestMethod.POST)
    public ViewerResult findAppForSelect(HttpServletRequest request, @RequestBody JSONObject obj) {
        ViewerResult result = new ViewerResult();
        List<UmsApp> appList = null;
        ListVO<AppForSelectVO> listVO = null;
        try {
            Object currentUsername = request.getAttribute("currentUsername");
            if (currentUsername == null) {
                appList = new ArrayList<UmsApp>();
            } else {
                String username = (String) currentUsername;
                if (Arrays.asList(FayUserConstant.SUPER_MANAGE_USERNAME).contains(username) || roleService.validate(username, FaySysRoleConstant.SUPER_MANAGE_ROLE_SN)) {
                    Searchable searchable = Searchable.newSearchable();
                    appList = umsAppFacade.list(searchable);
                } else {
                    UmsUser user = umsUserFacade.findByUsername((String) currentUsername);
                    appList = umsAppFacade.findAppByUserRoleRelation(user.getId(), AvailableEnum.TRUE.getValue(), FaySysRoleConstant.MANAGE_ROLE_SN_PREFIX);
                }
            }
            listVO = new ListVO<>(appList, AppForSelectVO.class);
            result.setSuccess(true);
            result.setData(listVO);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/findAppForAuthSelect", method = RequestMethod.POST)
    public ViewerResult findAppForAuthSelect(HttpServletRequest request, @RequestBody JSONObject obj) {
        ViewerResult result = new ViewerResult();
        List<UmsApp> appList = null;
        ListVO<AppForSelectVO> listVO = null;
        try {
            Object currentUsername = request.getAttribute("currentUsername");
            if (currentUsername == null) {
                appList = new ArrayList<UmsApp>();
                listVO = new ListVO<>(appList, AppForSelectVO.class);
            } else {
                String username = (String) currentUsername;
                if (Arrays.asList(FayUserConstant.SUPER_MANAGE_USERNAME).contains(username) || roleService.validate(username, FaySysRoleConstant.SUPER_MANAGE_ROLE_SN)) {
                    Searchable searchable = Searchable.newSearchable();
                    appList = umsAppFacade.list(searchable);
                    listVO = new ListVO<>(appList, AppForSelectVO.class);
                } else {
                    UmsUser user = umsUserFacade.findByUsername(username);
                    Searchable searchable = Searchable.newSearchable();
                    appList = umsAppFacade.list(searchable);
                    List<UmsApp> needApps = new ArrayList<>();
                    for (UmsApp app : appList) {
                        String appSn = app.getSn();
                        Set<UmsMenuResources> menuSet = umsMenuResFacade.getMenusByUserAndAppSn(user, appSn, AvailableEnum.TRUE.getValue());
                        if (menuSet.isEmpty()) {
                            Set<UmsControllerResources> controllerSet = umsControllerResFacade.getControllerResByUserAndAppSn(user, appSn, null, AvailableEnum.TRUE.getValue());
                            if (!controllerSet.isEmpty()) {
                                needApps.add(app);
                            }
                        } else {
                            needApps.add(app);
                        }
                    }
                    listVO = new ListVO<>(needApps, AppForSelectVO.class);
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
