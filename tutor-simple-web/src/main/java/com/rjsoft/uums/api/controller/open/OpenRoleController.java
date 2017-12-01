package com.rjsoft.uums.api.controller.open;

import com.alibaba.fastjson.JSONObject;
import com.rjsoft.common.entity.enums.AvailableEnum;
import com.rjsoft.common.model.search.Searchable;
import com.rjsoft.common.vo.ListVO;
import com.rjsoft.common.vo.ViewerResult;
import com.rjsoft.uums.api.controller.vo.open.role.OrgRoleVO;
import com.rjsoft.uums.api.controller.vo.open.role.RoleVO;
import com.rjsoft.uums.facade.app.entity.UmsApp;
import com.rjsoft.uums.facade.app.service.UmsAppFacade;
import com.rjsoft.uums.facade.auth.service.UmsAuthFacade;
import com.rjsoft.uums.facade.jwt.domain.Payload;
import com.rjsoft.uums.facade.jwt.service.UmsJwtFacade;
import com.rjsoft.uums.facade.orgRole.entity.UmsUserOrgRoleRelation;
import com.rjsoft.uums.facade.orgRole.service.UmsOrgRoleFacade;
import com.rjsoft.uums.facade.role.entity.UmsUserRoleRelation;
import com.rjsoft.uums.facade.role.service.UmsRoleFacade;
import com.rjsoft.uums.facade.user.entity.UmsUser;
import com.rjsoft.uums.facade.user.service.UmsUserFacade;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/open/role")
public class OpenRoleController {

    @Resource
    private UmsUserFacade umsUserFacade;

    @Resource
    private UmsRoleFacade umsRoleFacade;

    @Resource
    private UmsOrgRoleFacade umsOrgRoleFacade;

    @Resource
    private UmsAppFacade umsAppFacade;

    @Resource
    private UmsJwtFacade umsJwtFacade;

    @Resource
    private UmsAuthFacade umsAuthFacade;

    @RequestMapping(value = "/findSysRoles", method = RequestMethod.GET)
    public ViewerResult findSysRoles(HttpServletRequest request, HttpServletResponse response) {
        ViewerResult result = new ViewerResult();
        try {
            JSONObject obj = JSONObject.parseObject(request.getHeader("Authorization"));
            if (obj == null || obj.getString("appSn") == null) {
                request.getRequestDispatcher("/api/auth/pass/badRequest.do").forward(request, response);
            } else {
                String appSn = obj.getString("appSn");
                String jwt = obj.getString("token");
                if (jwt != null) {
                    Payload payload = umsJwtFacade.getPayload(jwt);
                    if (payload == null) {
                        request.getRequestDispatcher("/api/auth/pass/loginExpired.do").forward(request, response);
                    } else if (payload.getExp() > (new Date()).getTime()) {
                        String username = payload.getName();
                        UmsUser user = umsUserFacade.findByUsername(username);
                        if (user.getIsAvailable().equals(AvailableEnum.TRUE.getValue())) {
                            UmsApp app = umsAppFacade.getBySn(appSn);
                            Searchable searchable = Searchable.newSearchable();
                            searchable.addSearchParam("user.username_eq", username);
                            searchable.addSearchParam("role.appId_eq", app.getId());
                            searchable.addSearchParam("role.isAvailable_eq", AvailableEnum.TRUE.getValue());
                            List<UmsUserRoleRelation> uurrs = umsRoleFacade.listUmsUserRoleRelation(searchable);
                            ListVO<RoleVO> list = new ListVO<>(uurrs, RoleVO.class);
                            result.setData(list);
                            result.setSuccess(true);
                        } else {
                            request.getRequestDispatcher("/api/auth/pass/userIsNotAvailable.do").forward(request, response);
                        }
                    } else {
                        request.getRequestDispatcher("/api/auth/pass/loginExpired.do").forward(request, response);
                    }
                } else {
                    request.getRequestDispatcher("/api/auth/pass/noLogin.do").forward(request, response);
                }
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage("获取系统角色信息失败");
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/findOrgRoles", method = RequestMethod.GET)
    public ViewerResult findOrgRoles(HttpServletRequest request, HttpServletResponse response) {
        ViewerResult result = new ViewerResult();
        try {
            JSONObject obj = JSONObject.parseObject(request.getHeader("Authorization"));
            if (obj == null || obj.getString("appSn") == null) {
                request.getRequestDispatcher("/api/auth/pass/badRequest.do").forward(request, response);
            } else {
                String appSn = obj.getString("appSn");
                String jwt = obj.getString("token");
                if (jwt != null) {
                    Payload payload = umsJwtFacade.getPayload(jwt);
                    if (payload == null) {
                        request.getRequestDispatcher("/api/auth/pass/loginExpired.do").forward(request, response);
                    } else if (payload.getExp() > (new Date()).getTime()) {
                        String username = payload.getName();
                        UmsUser user = umsUserFacade.findByUsername(username);
                        if (user.getIsAvailable().equals(AvailableEnum.TRUE.getValue())) {
                            UmsApp app = umsAppFacade.getBySn(appSn);
                            Searchable searchable = Searchable.newSearchable();
                            searchable.addSearchParam("user.username_eq", username);
                            searchable.addSearchParam("role.appId_eq", app.getId());
                            searchable.addSearchParam("role.isAvailable_eq", AvailableEnum.TRUE.getValue());
                            List<UmsUserOrgRoleRelation> uuorrs = umsOrgRoleFacade.listUmsUserRoleRelation(searchable);
                            ListVO<OrgRoleVO> list = new ListVO<>(uuorrs, OrgRoleVO.class);
                            result.setData(list);
                            result.setSuccess(true);
                        } else {
                            request.getRequestDispatcher("/api/auth/pass/userIsNotAvailable.do").forward(request, response);
                        }
                    } else {
                        request.getRequestDispatcher("/api/auth/pass/loginExpired.do").forward(request, response);
                    }
                } else {
                    request.getRequestDispatcher("/api/auth/pass/noLogin.do").forward(request, response);
                }
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage("获取机构角色信息失败");
            e.printStackTrace();
        }
        return result;
    }
}