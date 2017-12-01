package com.rjsoft.uums.api.controller.resource;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rjsoft.common.entity.enums.AvailableEnum;
import com.rjsoft.common.model.search.Searchable;
import com.rjsoft.common.vo.ListVO;
import com.rjsoft.common.vo.PageVO;
import com.rjsoft.common.vo.ViewerResult;
import com.rjsoft.uums.api.constant.FaySysRoleConstant;
import com.rjsoft.uums.api.constant.FayUserConstant;
import com.rjsoft.uums.api.controller.vo.resource.ControllerResourceForSelectVO;
import com.rjsoft.uums.api.controller.vo.resource.ControllerResourceForTreeSelectVO;
import com.rjsoft.uums.api.controller.vo.resource.ControllerResourceVO;
import com.rjsoft.uums.api.service.role.RoleService;
import com.rjsoft.uums.api.util.tree.FayTreeUtil;
import com.rjsoft.uums.api.util.tree.TreeUtil;
import com.rjsoft.uums.facade.app.entity.UmsApp;
import com.rjsoft.uums.facade.app.service.UmsAppFacade;
import com.rjsoft.uums.facade.resource.entity.UmsControllerResources;
import com.rjsoft.uums.facade.resource.entity.UmsMenuResources;
import com.rjsoft.uums.facade.resource.service.UmsControllerResFacade;
import com.rjsoft.uums.facade.resource.service.UmsMenuResFacade;
import com.rjsoft.uums.facade.user.entity.UmsUser;
import com.rjsoft.uums.facade.user.service.UmsUserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/controllerResource")
public class ControllerResourceController {

    @Resource
    private UmsMenuResFacade umsMenuResFacade;

    @Resource
    private UmsControllerResFacade umsControllerResFacade;

    @Resource
    private UmsAppFacade umsAppFacade;

    @Resource
    private UmsUserFacade umsUserFacade;

    @Autowired
    private RoleService roleService;

    /**
     * get all menu resources by conditions for page
     *
     * @param name
     * @param number
     * @param size
     * @return
     */
    @RequestMapping(value = "/findForPage", method = RequestMethod.POST)
    public ViewerResult findForPage(@RequestBody JSONObject obj) {
        ViewerResult result = new ViewerResult();
        Page<UmsControllerResources> pageControllerResources = null;
        PageVO<ControllerResourceVO> pageVO = null;
        try {
            //get JSON format parameters
            String name = obj.getString("name");
            String appId = obj.getString("appId");
            int number = obj.getInteger("number");
            int size = obj.getInteger("size");
            Pageable page = PageRequest.of(number, size);
            Searchable searchable = Searchable.newSearchable();
            searchable.setPage(page);
            searchable.addSearchParam("controllerName_like", name);
            searchable.addSearchParam("application.id_eq", appId);
            //get all menu resources by conditions
            pageControllerResources = umsControllerResFacade.listPageUmsControllerRes(searchable);
            //convert to PageVO for view
            pageVO = new PageVO<>(pageControllerResources, ControllerResourceVO.class);
            result.setSuccess(true);
            result.setData(pageVO);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/findInTree", method = RequestMethod.POST)
    public ViewerResult findInTree(HttpServletRequest request, @RequestBody JSONObject obj) {
        ViewerResult result = new ViewerResult();
        List<UmsControllerResources> listOrg = null;
        ListVO<ControllerResourceVO> listVO = null;
        List<UmsApp> appList = null;
        try {
            String name = obj.getString("name");
            String appId = obj.getString("appId");
            String menuId = obj.getString("menuId");
            Searchable searchable = Searchable.newSearchable();

            Object currentUsername = request.getAttribute("currentUsername");
            if (currentUsername == null) {
                appList = new ArrayList<UmsApp>();
            } else {
                String username = (String) currentUsername;
                if (Arrays.asList(FayUserConstant.SUPER_MANAGE_USERNAME).contains(username) || roleService.validate(username, FaySysRoleConstant.SUPER_MANAGE_ROLE_SN)) {
                    searchable.addSearchParam("application.id_eq", appId);
                } else {
                    UmsUser user = umsUserFacade.findByUsername((String) currentUsername);
                    appList = umsAppFacade.findAppByUserRoleRelation(user.getId(), AvailableEnum.TRUE.getValue(), FaySysRoleConstant.MANAGE_ROLE_SN);
                    List<String> appIds = new ArrayList<>();
                    boolean flag = true;
                    for (UmsApp app : appList) {
                        appIds.add(app.getId());
                        if (app.getId().equals(appId)) {
                            flag = false;
                            searchable.addSearchParam("application.id_eq", appId);
                            break;
                        }
                    }
                    if (flag)
                        searchable.addSearchParam("application.id_in", appIds);
                }
                searchable.addSearchParam("menu.id_eq", menuId);
            }
            searchable.addSort(Direction.ASC, "controllerOrder");
            searchable.addSearchParam("controllerName_like", name);
            listOrg = umsControllerResFacade.listUmsControllerRes(searchable);
            listVO = new ListVO<>(listOrg, ControllerResourceVO.class);

            Object data = FayTreeUtil.getTreeInJsonObject(listVO.getVoList());

            result.setSuccess(true);
            result.setData(data);
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
        UmsControllerResources cr = null;
        try {
            String id = obj.getString("id");
            cr = umsControllerResFacade.findById(id);
            ControllerResourceVO controllerResourceVO = new ControllerResourceVO();
            controllerResourceVO.convertPOToVO(cr);
            result.setSuccess(true);
            result.setData(controllerResourceVO);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ViewerResult add(@RequestBody UmsControllerResources cr) {
        ViewerResult result = new ViewerResult();
        try {
            if (cr.getParent() != null && cr.getParent().getId() == null) {
                cr.setParent(null);
            }
            if (cr.getMenu() != null && cr.getMenu().getId() == null) {
                cr.setMenu(null);
            }
            cr = umsControllerResFacade.create(cr);
            ControllerResourceVO controllerResourceVO = new ControllerResourceVO();
            controllerResourceVO.convertPOToVO(cr);
            result.setSuccess(true);
            result.setData(controllerResourceVO);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ViewerResult delete(@RequestBody JSONObject obj) {
        ViewerResult result = new ViewerResult();
        try {
            JSONArray ja = obj.getJSONArray("ids");
            String[] ids = ja.toJavaObject(String[].class);
            umsControllerResFacade.deleteControllerRes(ids);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ViewerResult update(@RequestBody UmsControllerResources cr) {
        ViewerResult result = new ViewerResult();
        UmsControllerResources currentCr = null;
        try {
            currentCr = umsControllerResFacade.findById(cr.getId());
            currentCr.setControllerName(cr.getControllerName());
            currentCr.setControllerSn(cr.getControllerSn());
            currentCr.setControllerUrlMapping(cr.getControllerUrlMapping());
            currentCr.setApplication(cr.getApplication());
            currentCr.setControllerOrder(cr.getControllerOrder());
            if (cr.getParent() != null && cr.getParent().getId() != null) {
                String parentId = cr.getParent().getId();
                UmsControllerResources parent = umsControllerResFacade.findById(parentId);
                currentCr.setParent(parent);
            } else {
                currentCr.setParent(null);
            }
            if (cr.getMenu() != null && cr.getMenu().getId() != null) {
                String menuId = cr.getMenu().getId();
                UmsMenuResources menu = umsMenuResFacade.findById(menuId);
                currentCr.setMenu(menu);
            } else {
                currentCr.setMenu(null);
            }

            cr = umsControllerResFacade.update(currentCr);
            ControllerResourceVO controllerResourceVO = new ControllerResourceVO();
            controllerResourceVO.convertPOToVO(cr);
            result.setSuccess(true);
            result.setData(controllerResourceVO);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/updAvailable", method = RequestMethod.POST)
    public ViewerResult updAvailable(@RequestBody JSONObject obj) {
        ViewerResult result = new ViewerResult();
        try {
            String id = obj.getString("id");
            short isAvailable = new Short(obj.getString("isAvailable"));
            UmsControllerResources cr = umsControllerResFacade.updAvailable(id, isAvailable);
            ControllerResourceVO controllerResourceVO = new ControllerResourceVO();
            controllerResourceVO.convertPOToVO(cr);
            result.setSuccess(true);
            result.setData(controllerResourceVO);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * get all controller resources by conditions for select
     *
     * @return
     */
    @RequestMapping(value = "/findForSelect", method = RequestMethod.POST)
    public ViewerResult findForSelect(@RequestBody JSONObject obj) {
        ViewerResult result = new ViewerResult();
        List<UmsControllerResources> controllerResourcesList = null;
        ListVO<ControllerResourceForSelectVO> listVO = null;
        try {
            //get JSON format parameters
            String appId = obj.getString("appId");
            Searchable searchable = Searchable.newSearchable();
            searchable.addSearchParam("application.id_eq", appId);
            searchable.addSort(Direction.ASC, "controllerOrder");
            //get all by conditions
            controllerResourcesList = umsControllerResFacade.listUmsControllerRes(searchable);
            //convert to ListVO for view
            listVO = new ListVO<>(controllerResourcesList, ControllerResourceForSelectVO.class);
            result.setSuccess(true);
            result.setData(listVO);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/findForTreeSelect", method = RequestMethod.POST)
    public ViewerResult findForTreeSelect(@RequestBody JSONObject obj) {
        ViewerResult result = new ViewerResult();
        List<UmsControllerResources> controllerResourcesList = null;
        ListVO<ControllerResourceForTreeSelectVO> listVO = null;
        try {
            //get JSON format parameters
            String appId = obj.getString("appId");
            String deleteId = obj.getString("deleteId");
            Searchable searchable = Searchable.newSearchable();
            searchable.addSearchParam("application.id_eq", appId);
            searchable.addSearchParam("id_ne", deleteId);
            searchable.addSort(Direction.ASC, "controllerOrder");
            //get all by conditions
            controllerResourcesList = umsControllerResFacade.listUmsControllerRes(searchable);
            //convert to ListVO for view
            listVO = new ListVO<>(controllerResourcesList, ControllerResourceForTreeSelectVO.class);

            Object data = TreeUtil.getTreeSelectInJsonObject(listVO.getVoList());
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
