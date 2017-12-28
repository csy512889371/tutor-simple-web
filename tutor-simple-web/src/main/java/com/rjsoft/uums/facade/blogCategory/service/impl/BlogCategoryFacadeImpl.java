package com.rjsoft.uums.facade.blogCategory.service.impl;

import com.rjsoft.uums.core.blogCategory.service.BlogCategoryService;
import com.rjsoft.uums.facade.blogCategory.entity.BlogCategory;
import com.rjsoft.uums.facade.blogCategory.service.BlogCategoryFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("blogCategoryFacadeImpl")
public class BlogCategoryFacadeImpl implements BlogCategoryFacade {

    @Autowired
    private BlogCategoryService blogCategoryService;

    @Override
    public List<BlogCategory> findAll() {
        return blogCategoryService.findAll();
    }
}
