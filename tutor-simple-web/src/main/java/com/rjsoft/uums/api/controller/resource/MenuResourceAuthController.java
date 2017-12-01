package com.rjsoft.uums.api.controller.resource;

import com.rjsoft.common.entity.enums.AvailableEnum;
import com.rjsoft.common.model.search.Searchable;
import com.rjsoft.common.vo.ListVO;
import com.rjsoft.common.vo.ViewerResult;
import com.rjsoft.uums.api.constant.FaySysRoleConstant;
import com.rjsoft.uums.api.constant.FayUserConstant;
import com.rjsoft.uums.api.controller.vo.resource.MenuResourceForNavVO;
import com.rjsoft.uums.api.service.role.RoleService;
import com.rjsoft.uums.facade.auth.service.UmsAuthFacade;
import com.rjsoft.uums.facade.resource.entity.UmsMenuResources;
import com.rjsoft.uums.facade.resource.service.UmsMenuResFacade;
import com.rjsoft.uums.facade.user.entity.UmsUser;
import com.rjsoft.uums.facade.user.service.UmsUserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api/menuResource/auth")
public class MenuResourceAuthController {

    @Resource
    private UmsMenuResFacade umsMenuResFacade;

    @Resource
    private UmsAuthFacade umsAuthFacade;

    @Resource
    private UmsUserFacade umsUserFacade;

    @Autowired
    private RoleService roleService;

    /**
     * get all authorized menu by conditions
     *
     * @return
     */
    @RequestMapping(value = "/findMenu", method = RequestMethod.POST)
    public ViewerResult findMenu(HttpServletRequest request) {
        ViewerResult result = new ViewerResult();
        Set<UmsMenuResources> umsMenuResources = null;
        List<UmsMenuResources> umsMenuResourceList = null;
        ListVO<MenuResourceForNavVO> listVO = null;
        try {
            Object currentUsername = request.getAttribute("currentUsername");
            if (currentUsername == null) {
                umsMenuResources = new HashSet<>();
                listVO = new ListVO<>(umsMenuResources, MenuResourceForNavVO.class);
            } else {
                String username = (String) currentUsername;
                String appSn = (String) request.getAttribute("currentAppSn");
                if (Arrays.asList(FayUserConstant.SUPER_MANAGE_USERNAME).contains(username) || roleService.validate(username, FaySysRoleConstant.SUPER_MANAGE_ROLE_SN)) {
                    Searchable searchable = Searchable.newSearchable();
                    searchable.addSearchParam("isAvailable_eq", AvailableEnum.TRUE.getValue());
                    searchable.addSearchParam("application.sn_eq", appSn);
                    searchable.addSort(Direction.ASC, "menuOrder");
                    umsMenuResourceList = umsMenuResFacade.listUmsMenuResources(searchable);
                    listVO = new ListVO<>(umsMenuResourceList, MenuResourceForNavVO.class);
                } else {
                    UmsUser user = umsUserFacade.findByUsername(username);
                    umsMenuResources = umsMenuResFacade.getMenusByUserAndAppSn(user, appSn, AvailableEnum.TRUE.getValue());
                    List<UmsMenuResources> list = new ArrayList<>(umsMenuResources);
                    Collections.sort(list, new Comparator<UmsMenuResources>() {
                        @Override
                        public int compare(UmsMenuResources o1, UmsMenuResources o2) {
                            if (o1 instanceof UmsMenuResources && o2 instanceof UmsMenuResources) {
                                return o1.getMenuOrder() - o2.getMenuOrder();
                            }
                            throw new ClassCastException("不能转换为UmsMenuResources类型");
                        }
                    });

                    listVO = new ListVO<>(list, MenuResourceForNavVO.class);
                }
            }
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
