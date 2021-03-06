package com.rjsoft.uums.api.controller.blogVideo;

import com.alibaba.fastjson.JSONObject;
import com.rjsoft.common.model.search.Searchable;
import com.rjsoft.common.vo.ViewerResult;
import com.rjsoft.uums.facade.blogVideo.entity.BlogVideo;
import com.rjsoft.uums.facade.blogVideo.service.BlogVideoFacade;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/blog/video")
public class BlogVideoController {
    private static final Logger LOG = LoggerFactory.getLogger(BlogVideoController.class);

    @Resource
    private BlogVideoFacade blogVideoFacade;

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/findForPageByTopic", method = RequestMethod.POST)
    public ViewerResult findForPageByTopic(@RequestBody JSONObject obj) {
        ViewerResult result = new ViewerResult();
        try {
            String topicId = (String) obj.get("topicId");
            int number = obj.getInteger("number");
            int size = obj.getInteger("size");
            Pageable page = PageRequest.of(number, size);
            Searchable searchable = Searchable.newSearchable();
            searchable.setPage(page);

            if (StringUtils.isEmpty(topicId)) {
                Page<BlogVideo> topicPage = blogVideoFacade.listPage(searchable);
                result.setData(topicPage);
            } else {
                Page<BlogVideo> topicList = blogVideoFacade.findForPageByTopicId(topicId, page);
                result.setData(topicList);
            }

            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage("查找视频失败");
            LOG.error(e.getMessage(), e);
        }
        return result;
    }


    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/findAllForPage", method = RequestMethod.POST)
    public ViewerResult findAllForPage(@RequestBody JSONObject obj) {
        ViewerResult result = new ViewerResult();
        try {
            int number = obj.getInteger("number");
            int size = obj.getInteger("size");
            Pageable page = PageRequest.of(number, size);
            Page<BlogVideo> topicPage = blogVideoFacade.findAllForPage(page);
            result.setData(topicPage);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage("查找视频失败");
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/findForPageByName", method = RequestMethod.POST)
    public ViewerResult findForPageByName(@RequestBody JSONObject obj) {
        ViewerResult result = new ViewerResult();
        try {
            String name = (String) obj.get("name");
            int number = obj.getInteger("number");
            int size = obj.getInteger("size");
            Pageable page = PageRequest.of(number, size);
            Searchable searchable = Searchable.newSearchable();
            searchable.setPage(page);

            if (StringUtils.isNotEmpty(name)) {
                Page<BlogVideo> topicList = blogVideoFacade.findForPageByTopicName(name, page);
                result.setData(topicList);
            }
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage("根据名称查找视频失败");
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/viewVideo", method = RequestMethod.POST)
    public ViewerResult viewVideo(@RequestBody JSONObject obj) {
        ViewerResult result = new ViewerResult();
        try {
            String videoId = (String) obj.get("videoId");
            blogVideoFacade.addOneViewCount(videoId);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage("更新查看次数失败");
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    @RequestMapping(value = "/findById", method = RequestMethod.POST)
    public ViewerResult findById(@RequestBody JSONObject obj) {
        ViewerResult result = new ViewerResult();
        BlogVideo video = null;
        try {
            String id = obj.getString("id");
            video = blogVideoFacade.getById(id);
            result.setSuccess(true);
            result.setData(video);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

}
