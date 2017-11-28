package com.rjsoft.uums.api.controller.app;

import com.rjsoft.common.vo.ViewerResult;
import com.rjsoft.uums.api.constant.FaySysRoleConstant;
import com.rjsoft.uums.api.controller.vo.app.AppVO;
import com.rjsoft.uums.facade.app.entity.UmsApp;
import com.rjsoft.uums.facade.app.service.UmsAppFacade;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping("/api/app")
@RestController
public class AppController {

    @Resource(name = "umsAppFacadeImpl")
    private UmsAppFacade umsAppFacade;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    ViewerResult add(UmsApp app) {
        ViewerResult result = new ViewerResult();
        try {
            app = umsAppFacade.register(app);
            String appId = app.getId();
            String appSn = app.getSn();
            String[] appDefaultRolesPrefix = FaySysRoleConstant.DEFAULT_ROLE_SN_PREFIX;
            String[] appDefaultRolesName = FaySysRoleConstant.DEFAULT_ROLE_NAME;
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
