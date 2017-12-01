package com.rjsoft.uums.api.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.rjsoft.common.utils.DateUtil;
import com.rjsoft.common.utils.NetworkUtil;
import com.rjsoft.common.vo.ViewerResult;
import com.rjsoft.uums.facade.jwt.service.UmsJwtFacade;
import com.rjsoft.uums.facade.log.entity.UmsLog;
import com.rjsoft.uums.facade.log.enums.LogLevelEnum;
import com.rjsoft.uums.facade.log.enums.LogTypeEnum;
import com.rjsoft.uums.facade.log.enums.OpResultEnum;
import com.rjsoft.uums.facade.log.service.UmsLogFacade;
import com.rjsoft.uums.facade.user.service.UmsUserFacade;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * user api
 *
 * @author feichongzheng
 */
@RestController
@RequestMapping("/api/user/logout")
public class UserLogoutController {

    @Resource
    private UmsUserFacade umsUserFacade;

    @Resource
    private UmsJwtFacade umsJwtFacade;

    @Resource
    private UmsLogFacade umsLogFacade;

    /**
     * logout
     *
     * @param obj
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ViewerResult logout(@RequestBody JSONObject obj, HttpServletRequest request) {
        ViewerResult result = new ViewerResult();
        String username = null;
        String jwt = null;
        LocalDateTime opTime = DateUtil.utilDateToLocalDateTime(new Date());
        long startTime = System.currentTimeMillis();
        try {
            username = obj.getString("username");
            jwt = obj.getString("token");
            umsJwtFacade.removeJwt(username, jwt);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        try {
            logout(username, endTime - startTime, opTime, jwt, result.isSuccess(), request);
        } catch (Exception e) {
        }
        return result;
    }

    private void logout(String username, long execTime, LocalDateTime opTime, String jwt, boolean success, HttpServletRequest request) {
        try {
            UmsLog umsLog = new UmsLog();
            umsLog.setBackEndAccessPath("/uums/api/user/logout");
            umsLog.setBrowser(NetworkUtil.getBrowser(request));
            umsLog.setExecTime(execTime);
            umsLog.setIp(NetworkUtil.getIpAddress(request));
            umsLog.setLogLevel(LogLevelEnum.NORMAL.getValue());
            umsLog.setLogType(LogTypeEnum.LOGOUT.getValue());
            umsLog.setOpResource("用户登出");
            String desc = "用户登出：用户名【" + username + "】、令牌【" + jwt + "】";
            umsLog.setOpDesc(desc);
            umsLog.setOpResult(success ? OpResultEnum.SUCCESS.getValue() : OpResultEnum.FAIl.getValue());
            umsLog.setOpSystem(NetworkUtil.getOS(request));
            umsLog.setOpTime(opTime);
            umsLog.setUsername(username);
            umsLogFacade.save(umsLog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}