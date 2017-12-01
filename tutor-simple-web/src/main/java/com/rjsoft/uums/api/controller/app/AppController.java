package com.rjsoft.uums.api.controller.app;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rjsoft.common.model.search.Searchable;
import com.rjsoft.common.vo.PageVO;
import com.rjsoft.common.vo.ViewerResult;
import com.rjsoft.uums.api.constant.FaySysRoleConstant;
import com.rjsoft.uums.api.constant.FayUserConstant;
import com.rjsoft.uums.api.controller.vo.app.AppVO;
import com.rjsoft.uums.api.service.role.RoleService;
import com.rjsoft.uums.facade.app.entity.UmsApp;
import com.rjsoft.uums.facade.app.service.UmsAppFacade;
import com.rjsoft.uums.facade.resource.service.UmsControllerResFacade;
import com.rjsoft.uums.facade.role.entity.UmsRole;
import com.rjsoft.uums.facade.role.service.UmsRoleFacade;
import com.rjsoft.uums.facade.user.entity.UmsUser;
import com.rjsoft.uums.facade.user.service.UmsUserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/app")
public class AppController {

    @Resource(name = "umsAppFacadeImpl")
    private UmsAppFacade umsAppFacade;

    @Resource(name = "umsControllerResFacadeImpl")
    private UmsControllerResFacade umsControllerResFacade;

    @Resource(name = "umsUserFacadeImpl")
    private UmsUserFacade umsUserFacade;

    @Resource(name = "umsRoleFacadeImpl")
    private UmsRoleFacade umsRoleFacade;

    @Autowired
    private RoleService roleService;

    /**
     * get all apps by conditions for page
     *
     * @return
     */
    @RequestMapping(value = "/findForPage", method = RequestMethod.POST)
    public ViewerResult findForPage(HttpServletRequest request, @RequestBody JSONObject obj) {
        ViewerResult result = new ViewerResult();
        Page<UmsApp> pageApp = null;
        PageVO<AppVO> pageVO = null;
        try {
            Object currentUsername = request.getAttribute("currentUsername");
            if (currentUsername == null) {
                pageVO = new PageVO<>(new ArrayList<>(), AppVO.class);
            } else {
                String username = (String) currentUsername;
                String name = obj.getString("name");
                int number = obj.getInteger("number");
                int size = obj.getInteger("size");
                Pageable page = PageRequest.of(number, size);
                if (Arrays.asList(FayUserConstant.SUPER_MANAGE_USERNAME).contains(username) || roleService.validate(username, FaySysRoleConstant.SUPER_MANAGE_ROLE_SN)) {
                    Searchable searchable = Searchable.newSearchable();
                    searchable.setPage(page);
                    searchable.addSearchParam("name_like", name);
                    //get all apps by conditions
                    pageApp = umsAppFacade.listPage(searchable);
                } else {
                    UmsUser user = umsUserFacade.findByUsername((String) currentUsername);
                    if (name == null) name = "";
                    pageApp = umsAppFacade.listPage(name, user.getId(), FaySysRoleConstant.MANAGE_ROLE_SN_PREFIX, page);
                }
                pageVO = new PageVO<>(pageApp, AppVO.class);
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
        UmsApp app = null;
        try {
            String id = obj.getString("id");
            app = umsAppFacade.getById(id);
            AppVO appVO = new AppVO();
            appVO.convertPOToVO(app);
            result.setSuccess(true);
            result.setData(appVO);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ViewerResult add(UmsApp app) {
        ViewerResult result = new ViewerResult();
        try {
            app = umsAppFacade.register(app);
            String appId = app.getId();
            String appSn = app.getSn();
            String[] appDefaultRolesPrefix = FaySysRoleConstant.DEFAULT_ROLE_SN_PREFIX;
            String[] appDefaultRolesName = FaySysRoleConstant.DEFAULT_ROLE_NAME;
            for (int i = 0; i < appDefaultRolesPrefix.length; i++) {
                UmsRole role = new UmsRole();
                role.setAppId(appId);
                role.setName(appDefaultRolesName[i]);
                role.setSn(appDefaultRolesPrefix[i] + appSn);
                umsRoleFacade.create(role);

            }
            AppVO appVO = new AppVO();
            appVO.convertPOToVO(app);
            result.setSuccess(true);
            result.setData(appVO);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ViewerResult delete(@RequestBody JSONObject obj) {
        ViewerResult result = new ViewerResult();
        try {
            JSONArray ja = obj.getJSONArray("ids");
            List<String> ids = ja.toJavaObject(List.class);
            umsAppFacade.delete(ids);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ViewerResult update(UmsApp app) {
        ViewerResult result = new ViewerResult();
        try {
            UmsApp oldApp = umsAppFacade.getById(app.getId());
            oldApp.setDescription(app.getDescription());
            oldApp.setName(app.getName());
            oldApp.setSsoFlag(app.getSsoFlag());
            oldApp.setUrl(app.getUrl());
            app = umsAppFacade.update(oldApp);
            AppVO appVO = new AppVO();
            appVO.convertPOToVO(app);
            result.setSuccess(true);
            result.setData(appVO);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
