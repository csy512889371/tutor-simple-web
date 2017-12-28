package com.rjsoft.uums.facade.blogArticle.entity;

import com.rjsoft.common.entity.UUIDEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "EVA_BLOG_ARTICLE")
public class BlogArticle extends UUIDEntity<String> {

    /**
     * 文章名称
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 文章内容
     */
    @Column(name = "CONTENT")
    private String content;

    /**
     * 收藏数量
     */
    @Column(name = "COLLECTION")
    private int collection;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCollection() {
        return collection;
    }

    public void setCollection(int collection) {
        this.collection = collection;
    }
}
