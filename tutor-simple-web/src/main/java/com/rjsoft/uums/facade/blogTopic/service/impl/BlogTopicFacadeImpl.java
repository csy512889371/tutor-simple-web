package com.rjsoft.uums.facade.blogTopic.service.impl;

import com.rjsoft.common.model.search.Searchable;
import com.rjsoft.uums.core.blogTopic.service.BlogTopicService;
import com.rjsoft.uums.facade.blogTopic.entity.BlogTopic;
import com.rjsoft.uums.facade.blogTopic.service.BlogTopicFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("blogTopicFacadeImpl")
public class BlogTopicFacadeImpl implements BlogTopicFacade {

    @Autowired
    private BlogTopicService blogTopicService;


    @Override
    public List<BlogTopic> findTopicByCategoryId(String categoryId) {
        return blogTopicService.findTopicByCategoryId(categoryId);
    }

    @Override
    public Page<BlogTopic> findTopicByCategoryIds(List<String> categoryIds, Pageable page) {
        return blogTopicService.findTopicByCategoryIds(categoryIds, page);
    }

    @Override
    public List<BlogTopic> findAllTopic() {
        return blogTopicService.findAll();
    }

    @Override
    public Page<BlogTopic> listPage(Searchable searchable) {
        return blogTopicService.findAll(searchable);
    }

    @Override
    public BlogTopic getById(String topicId) {
        return blogTopicService.getById(topicId);
    }


}
