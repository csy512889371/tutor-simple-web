package com.rjsoft.uums.api.controller.common;

import com.alibaba.fastjson.JSONObject;
import com.rjsoft.common.model.search.Searchable;
import com.rjsoft.common.vo.ListVO;
import com.rjsoft.common.vo.ViewerResult;
import com.rjsoft.uums.api.controller.vo.common.AppForSelectVO;
import com.rjsoft.uums.facade.app.entity.UmsApp;
import com.rjsoft.uums.facade.app.service.UmsAppFacade;
import com.rjsoft.uums.facade.position.service.UmsPositionFacade;
import com.rjsoft.uums.facade.resource.service.UmsMenuResFacade;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/common")
public class CommonController {

    @Resource
    private UmsMenuResFacade umsMenuResFacade;

    @Resource
    private UmsAppFacade umsAppFacade;

    @Resource
    private UmsPositionFacade umsPositionFacade;

    @RequestMapping(value = "/findAppForSelect", method = RequestMethod.POST)
    public ViewerResult findAppForSelect(@RequestBody JSONObject obj) {
        ViewerResult result = new ViewerResult();
        List<UmsApp> appList = null;
        ListVO<AppForSelectVO> listVO = null;
        try {
            Searchable searchable = Searchable.newSearchable();
            //get all app by conditions
            appList = umsAppFacade.list(searchable);
            //convert to ListVO for view
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
}