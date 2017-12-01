package com.rjsoft.uums.core.resource.service;

import com.rjsoft.common.service.BaseService;
import com.rjsoft.uums.core.auth.repository.UmsAclRepository;
import com.rjsoft.uums.core.resource.repository.UmsButtonRepository;
import com.rjsoft.uums.core.resource.repository.UmsMenuButtonRepository;
import com.rjsoft.uums.facade.Resource;
import com.rjsoft.uums.facade.auth.entity.UmsAcl;
import com.rjsoft.uums.facade.resource.entity.UmsButton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("umsButtonService")
public class UmsButtonService extends BaseService<UmsButton, String> {

    @Autowired
    private UmsButtonRepository umsButtonRepository;
    @Autowired
    private UmsMenuButtonRepository umsMenuButtonRepository;
    @Autowired
    private UmsAclRepository umsAclRepository;

    /**
     * 根据菜单获取所有可用按钮
     *
     * @param menuId
     * @return
     */
    public List<UmsButton> findButtonsByMenuId(String menuId, Short isAvailable) {
        return umsButtonRepository.findButtonsByMenuId(menuId, isAvailable);
    }

    /**
     * 删除按钮
     *
     * @param ids
     */
    public void deleteButton(String... ids) {
        for (String id : ids) {
            UmsButton umsButton = umsButtonRepository.getOne(id);
            List<String> menuIdList = umsMenuButtonRepository.findMenuIdByButtonId(id);
            if (menuIdList != null && menuIdList.size() > 0) {
                for (String menuId : menuIdList) {
                    List<UmsAcl> acls = umsAclRepository.getAclByResource(menuId, Resource.RESOURCE_MENU);
                    for (UmsAcl acl : acls) {
                        acl.setPermission(umsButton.getButtonOrder(), false);
                        umsAclRepository.save(acl);
                    }
                }
                umsMenuButtonRepository.deleteMenuButtonByButtonId(id);
            }
            delete(id);
        }
    }


}
