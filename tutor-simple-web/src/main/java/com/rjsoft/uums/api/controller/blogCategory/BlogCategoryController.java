package com.rjsoft.uums.api.controller.blogCategory;

import com.rjsoft.common.vo.ViewerResult;
import com.rjsoft.uums.facade.blogCategory.service.BlogCategoryFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/blog/category")
public class BlogCategoryController {
    private static final Logger LOG = LoggerFactory.getLogger(BlogCategoryController.class);

    @Resource
    private BlogCategoryFacade blogCategoryFacade;

    @RequestMapping(value = "/findAll", method = RequestMethod.POST)
    public ViewerResult findAll() {
        ViewerResult result = new ViewerResult();
        try {
            result.setData(blogCategoryFacade.findAll());
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage("查找主题类别失败");
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

}
