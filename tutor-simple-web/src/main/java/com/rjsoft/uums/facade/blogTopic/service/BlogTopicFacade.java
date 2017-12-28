package com.rjsoft.uums.facade.blogTopic.service;

import com.rjsoft.common.model.search.Searchable;
import com.rjsoft.uums.facade.blogTopic.entity.BlogTopic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BlogTopicFacade {

    List<BlogTopic> findTopicByCategoryId(String categoryId);

    /**
     * 根据类别查询主题列表
     *
     * @param categoryIds 列表
     * @param page        分页信息
     * @return 主题列表
     */
    Page<BlogTopic> findTopicByCategoryIds(List<String> categoryIds, Pageable page);


    List<BlogTopic> findAllTopic();

    /**
     * 分页查询
     *
     * @param searchable 分页信息
     * @return 视频列表
     */
    Page<BlogTopic> listPage(Searchable searchable);

    BlogTopic getById(String topicId);

}
