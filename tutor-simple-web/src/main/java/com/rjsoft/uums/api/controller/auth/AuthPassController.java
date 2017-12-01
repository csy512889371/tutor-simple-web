package com.rjsoft.uums.api.controller.auth;

import com.rjsoft.common.vo.ViewerResult;
import com.rjsoft.uums.api.http.ResponseStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth/pass")
public class AuthPassController {

    @RequestMapping("/notAuth")
    public ViewerResult notAuth(HttpServletRequest request, HttpServletResponse response) {
        Object requestName = request.getAttribute("requestName");
        ViewerResult result = new ViewerResult();
        result.setSuccess(false);
        if (requestName == null) {
            result.setErrMessage("你无权访问");
        } else {
            result.setErrMessage((String) requestName + "，你无权访问");
        }
        response.setStatus(ResponseStatus.FORBIDDEN);
        return result;
    }

    @RequestMapping("/loginExpired")
    public ViewerResult loginExpired(HttpServletResponse response) {
        ViewerResult result = new ViewerResult();
        result.setSuccess(false);
        result.setErrMessage("账号已过期，请重新登录");
        response.setStatus(ResponseStatus.NEED_LOGIN_AGAIN);
        return result;
    }

    @RequestMapping("/noLogin")
    public ViewerResult noLogin(HttpServletResponse response) {
        ViewerResult result = new ViewerResult();
        result.setSuccess(false);
        result.setErrMessage("您尚未登录，请先登录");
        response.setStatus(ResponseStatus.NEED_LOGIN_AGAIN);
        return result;
    }

    @RequestMapping("/badRequest")
    public ViewerResult badRequest(HttpServletResponse response) {
        ViewerResult result = new ViewerResult();
        result.setSuccess(false);
        result.setErrMessage("发送的请求不正确");
        response.setStatus(ResponseStatus.BAD_REQUEST);
        return result;
    }

    @RequestMapping("/logoutAndRequestAgain")
    public ViewerResult logoutAndRequestAgain(HttpServletResponse response) {
        ViewerResult result = new ViewerResult();
        result.setSuccess(false);
        result.setErrMessage("退出登录后重新请求");
        response.setStatus(ResponseStatus.NEED_LOGIN_AGAIN);
        return result;
    }

    @RequestMapping("/userIsNotAvailable")
    public ViewerResult userIsNotAvailable(HttpServletResponse response) {
        ViewerResult result = new ViewerResult();
        result.setSuccess(false);
        result.setErrMessage("该用户已被禁用");
        response.setStatus(ResponseStatus.USER_FORBIDDEN);
        return result;
    }
}
