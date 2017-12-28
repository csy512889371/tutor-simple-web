package com.rjsoft.uums.facade.blogVideo.service.impl;

import com.rjsoft.common.model.search.Searchable;
import com.rjsoft.uums.core.blogVideo.service.BlogVideoService;
import com.rjsoft.uums.facade.blogVideo.entity.BlogVideo;
import com.rjsoft.uums.facade.blogVideo.service.BlogVideoFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service("blogVideoFacadeImpl")
public class BlogVideoFacadeImpl implements BlogVideoFacade {

    @Autowired
    private BlogVideoService blogVideoService;

    @Override
    public Page<BlogVideo> findForPageByTopicId(String topicId, Pageable page) {
        return blogVideoService.findForPageByTopicId(topicId, page);
    }

    @Override
    public Page<BlogVideo> findAllForPage(Pageable page) {
        return blogVideoService.findAllForPage(page);
    }

    @Override
    public Page<BlogVideo> listPage(Searchable searchable) {
        return blogVideoService.findAll(searchable);
    }

    @Override
    public BlogVideo getById(String videoId) {
        return blogVideoService.getById(videoId);
    }

    @Override
    public Page<BlogVideo> findForPageByTopicName(String name, Pageable page) {
        return blogVideoService.findForPageByTopicName(name, page);
    }

    @Override
    public void addOneViewCount(String videoId) {
        blogVideoService.addOneViewCount(videoId);
    }

}
