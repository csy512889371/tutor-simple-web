package com.rjsoft.uums.api.controller.unify;

import com.alibaba.fastjson.JSONObject;
import com.rjsoft.common.vo.ListVO;
import com.rjsoft.common.vo.ViewerResult;
import com.rjsoft.uums.api.controller.vo.unify.ResourceTreeVO;
import com.rjsoft.uums.api.service.appResource.AppResourceService;
import com.rjsoft.uums.api.util.tree.FayTreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/unify")
public class UnifyResourceController {

    @Autowired
    private AppResourceService appResourceService;

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/findResourceInTree", method = RequestMethod.POST)
    public ViewerResult findResourceInTree(HttpServletRequest request, @RequestBody JSONObject obj) {
        ViewerResult result = new ViewerResult();
        ListVO<ResourceTreeVO> listVO = null;
        Object data = null;
        try {
            Object currentUsername = request.getAttribute("currentUsername");
            if (currentUsername != null) {
                String username = (String) currentUsername;
                List<String> currentUserRoleSn = (List<String>) request.getAttribute("currentUserRoleSn");
                String name = obj.getString("name");
                data = appResourceService.findAppResourceInTreeByLoginUser(name, username, currentUserRoleSn);
            } else {
                listVO = new ListVO<>(new ArrayList<>(), ResourceTreeVO.class);
                data = FayTreeUtil.getTreeInJsonObject(listVO.getVoList());
            }
            result.setSuccess(true);
            result.setData(data);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}