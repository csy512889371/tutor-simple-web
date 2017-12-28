package com.rjsoft.uums.core.blogTopic.repository;

import com.rjsoft.common.repository.CustomRepository;
import com.rjsoft.uums.facade.blogTopic.entity.BlogTopic;
import com.rjsoft.uums.facade.blogVideo.entity.BlogVideo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BlogTopicRepository extends CustomRepository<BlogTopic, String> {

    @Query("select a from BlogTopic a where a.categoryId=?1 order by a.inino asc ")
    List<BlogTopic> findTopicByCategoryId(String categoryId);

    @Query("select a from BlogTopic a where a.categoryId in ?1 order by a.inino asc")
    Page<BlogTopic> findTopicByCategoryIds(List<String> categoryIds, Pageable page);

    @Query("select a from BlogTopic a where a.id=?1 ")
    BlogTopic findByTopicId(String id);
}
