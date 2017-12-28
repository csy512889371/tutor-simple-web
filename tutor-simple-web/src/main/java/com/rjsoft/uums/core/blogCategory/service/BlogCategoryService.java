package com.rjsoft.uums.core.blogCategory.service;

import com.rjsoft.common.service.BaseService;
import com.rjsoft.uums.core.blogCategory.repository.BlogCategoryRepository;
import com.rjsoft.uums.facade.blogCategory.entity.BlogCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlogCategoryService  extends BaseService<BlogCategory, String> {

    @Autowired
    private BlogCategoryRepository blogCategoryRepository;

}
