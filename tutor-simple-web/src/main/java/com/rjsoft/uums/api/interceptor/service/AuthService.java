package com.rjsoft.uums.api.interceptor.service;

import com.alibaba.fastjson.JSONObject;
import com.rjsoft.uums.api.constant.FaySysRoleConstant;
import com.rjsoft.uums.api.constant.FayUserConstant;
import com.rjsoft.uums.api.service.role.RoleService;
import com.rjsoft.uums.facade.auth.domain.Authorization;
import com.rjsoft.uums.facade.auth.service.UmsAuthFacade;
import com.rjsoft.uums.facade.user.entity.UmsUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Service
public class AuthService {

    @Resource
    private UmsAuthFacade umsAuthFacade;

    @Autowired
    private RoleService roleService;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String servletPath = request.getServletPath();
        servletPath = servletPath.replace(".do","");
        if (servletPath.equals("/api/user/login")
                || servletPath.startsWith("/api/user/logout")
                || servletPath.startsWith("/api/auth/pass")
                || servletPath.startsWith("/api/open")) {
            return true;
        }
        JSONObject obj = JSONObject.parseObject(request.getHeader("Authorization"));
        if (obj == null || obj.getString("appSn") == null) {
            request.getRequestDispatcher("/api/auth/pass/badRequest.do").forward(request, response);
            return false;
        } else {
            String appSn = obj.getString("appSn");
            request.setAttribute("currentAppSn", appSn);
            String jwt = obj.getString("token");
            Authorization authorization = umsAuthFacade.auth(servletPath, appSn, jwt);
            int status = authorization.getStatus();
            UmsUser user = authorization.getUser();
            if (status == 200) {
                if (user == null) {
                    if (jwt != null) {
                        request.getRequestDispatcher("/api/auth/pass/logoutAndRequestAgain.do").forward(request, response);
                        return false;
                    }
                } else {
                    request.setAttribute("currentUsername", user.getUsername());
                }
                return true;
            } else {
                if (user != null
                        && (Arrays.asList(FayUserConstant.SUPER_MANAGE_USERNAME).contains(user.getUsername())
                        || roleService.validate(user.getUsername(), FaySysRoleConstant.SUPER_MANAGE_ROLE_SN))) {
                    request.setAttribute("currentUsername", user.getUsername());
                    return true;
                }
                if (status == 400) {
                    request.getRequestDispatcher("/api/auth/pass/badRequest.do").forward(request, response);
                } else if (status == 4011) {
                    request.getRequestDispatcher("/api/auth/pass/loginExpired.do").forward(request, response);
                } else if (status == 4012) {
                    request.getRequestDispatcher("/api/auth/pass/noLogin.do").forward(request, response);
                } else if (status == 403) {
                    request.getRequestDispatcher("/api/auth/pass/notAuth.do").forward(request, response);
                } else if (status == 4031) {
                    request.getRequestDispatcher("/api/auth/pass/userIsNotAvailable.do").forward(request, response);
                }
                return false;
            }
        }
    }
}
