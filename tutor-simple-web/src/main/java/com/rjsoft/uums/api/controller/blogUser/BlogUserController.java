package com.rjsoft.uums.api.controller.blogUser;

import com.rjsoft.common.vo.ViewerResult;
import com.rjsoft.uums.api.constant.FaySysRoleConstant;
import com.rjsoft.uums.api.controller.vo.user.UserVO;
import com.rjsoft.uums.facade.role.entity.UmsRole;
import com.rjsoft.uums.facade.role.service.UmsRoleFacade;
import com.rjsoft.uums.facade.user.entity.UmsUser;
import com.rjsoft.uums.facade.user.service.UmsUserFacade;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/blog/user")
public class BlogUserController {

    @Resource
    private UmsUserFacade umsUserFacade;

    @Resource
    private UmsRoleFacade umsRoleFacade;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ViewerResult add(@RequestBody UmsUser user) {
        ViewerResult result = new ViewerResult();
        try {
            user = umsUserFacade.register(user);
            UmsRole role = umsRoleFacade.getBySn(FaySysRoleConstant.GENERAL_ROLE_SN_PREFIX + FaySysRoleConstant.CTO_EDU_APP_CODE);
            if (role != null) {
                umsRoleFacade.buildUserRoleRelation(role.getId(), new String[]{user.getId()});
            }

            UserVO userVO = new UserVO();
            userVO.convertPOToVO(user);
            result.setSuccess(true);
            result.setData(userVO);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

}
