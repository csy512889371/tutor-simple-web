package com.rjsoft.uums.api.controller.open;

import com.alibaba.fastjson.JSONObject;
import com.rjsoft.common.entity.enums.AvailableEnum;
import com.rjsoft.common.vo.ViewerResult;
import com.rjsoft.uums.facade.auth.domain.Authorization;
import com.rjsoft.uums.facade.auth.service.UmsAuthFacade;
import com.rjsoft.uums.facade.jwt.domain.Payload;
import com.rjsoft.uums.facade.jwt.service.UmsJwtFacade;
import com.rjsoft.uums.facade.resource.entity.UmsControllerResources;
import com.rjsoft.uums.facade.resource.entity.UmsMenuResources;
import com.rjsoft.uums.facade.resource.service.UmsControllerResFacade;
import com.rjsoft.uums.facade.resource.service.UmsMenuResFacade;
import com.rjsoft.uums.facade.user.entity.UmsUser;
import com.rjsoft.uums.facade.user.service.UmsUserFacade;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Set;

@RestController
@RequestMapping("/api/open/resource")
public class OpenResourceController {

    @Resource
    private UmsMenuResFacade umsMenuResFacade;

    @Resource
    private UmsControllerResFacade umsControllerResFacade;

    @Resource
    private UmsUserFacade umsUserFacade;

    @Resource
    private UmsJwtFacade umsJwtFacade;

    @Resource
    private UmsAuthFacade umsAuthFacade;

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public ViewerResult getAll(HttpServletRequest request, HttpServletResponse response) {
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
                        Set<UmsMenuResources> menus = umsMenuResFacade.getMenusByUserAndAppSn(user, appSn, AvailableEnum.TRUE.getValue());
                        Set<UmsControllerResources> controllers = umsControllerResFacade.getControllerResByUserAndAppSn(user, appSn, null, AvailableEnum.TRUE.getValue());
                        JSONObject object = new JSONObject();
                        object.put("menuResource", menus);
                        object.put("requestResource", controllers);
                        result.setData(object);
                        result.setSuccess(true);
                    } else {
                        request.getRequestDispatcher("/api/auth/pass/loginExpired.do").forward(request, response);
                    }
                } else {
                    request.getRequestDispatcher("/api/auth/pass/noLogin.do").forward(request, response);
                }
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage("获取权限资源失败");
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    public ViewerResult auth(String path, HttpServletRequest request, HttpServletResponse response) {
        ViewerResult result = new ViewerResult();
        try {
            JSONObject obj = JSONObject.parseObject(request.getHeader("Authorization"));
            if (obj == null) {
                request.getRequestDispatcher("/api/auth/pass/badRequest.do").forward(request, response);
            } else {
                String appSn = obj.getString("appSn");
                String token = obj.getString("token");
                request.setAttribute("currentAppSn", appSn);
                Authorization authorization = umsAuthFacade.auth(path, appSn, token);
                int status = authorization.getStatus();
                UmsUser user = authorization.getUser();
                JSONObject data = new JSONObject();
                data.put("status", status);
                if (user != null) data.put("username", user.getUsername());
                result.setSuccess(true);
                result.setData(data);
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage("获取权限资源失败");
            e.printStackTrace();
        }
        return result;
    }
}