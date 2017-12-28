package com.rjsoft.uums.facade.blogCategory.service;

import com.rjsoft.uums.facade.blogCategory.entity.BlogCategory;

import java.util.List;

public interface BlogCategoryFacade {

    List<BlogCategory> findAll();
}
