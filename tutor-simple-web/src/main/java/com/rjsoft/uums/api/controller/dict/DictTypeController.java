package com.rjsoft.uums.api.controller.dict;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rjsoft.common.model.search.Searchable;
import com.rjsoft.common.vo.ListVO;
import com.rjsoft.common.vo.PageVO;
import com.rjsoft.common.vo.ViewerResult;
import com.rjsoft.uums.api.controller.vo.dict.DictTypeForSelectVO;
import com.rjsoft.uums.api.controller.vo.dict.DictTypeVO;
import com.rjsoft.uums.facade.dict.entity.UmsDictionaryType;
import com.rjsoft.uums.facade.dict.service.UmsDictionaryTypeFacade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/dictType")
public class DictTypeController {

    @Resource
    private UmsDictionaryTypeFacade umsDictionaryTypeFacade;

    @RequestMapping(value = "/findForPage", method = RequestMethod.POST)
    public @ResponseBody
    ViewerResult findForPage(HttpServletRequest request, @RequestBody JSONObject obj) {
        ViewerResult result = new ViewerResult();
        Page<UmsDictionaryType> page = null;
        PageVO<DictTypeVO> pageVO = null;
        try {
            String name = obj.getString("name");
            int number = obj.getInteger("number");
            int size = obj.getInteger("size");
            Pageable pageable = PageRequest.of(number, size);
            Searchable searchable = Searchable.newSearchable();
            searchable.setPage(pageable);
            searchable.addSearchParam("value_like", name);
            page = umsDictionaryTypeFacade.listPage(searchable);
            pageVO = new PageVO<>(page, DictTypeVO.class);
            result.setSuccess(true);
            result.setData(pageVO);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/findForSelect", method = RequestMethod.POST)
    public ViewerResult findForSelect(HttpServletRequest request) {
        ViewerResult result = new ViewerResult();
        List<UmsDictionaryType> list = null;
        ListVO<DictTypeForSelectVO> listVO = null;
        try {
            Searchable searchable = Searchable.newSearchable();
            list = umsDictionaryTypeFacade.list(searchable);
            listVO = new ListVO<>(list, DictTypeForSelectVO.class);
            result.setSuccess(true);
            result.setData(listVO);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ViewerResult add(@RequestBody UmsDictionaryType UmsDictionaryType) {
        ViewerResult result = new ViewerResult();
        try {
            UmsDictionaryType = umsDictionaryTypeFacade.create(UmsDictionaryType);
            DictTypeVO dictTypeVO = new DictTypeVO();
            dictTypeVO.convertPOToVO(UmsDictionaryType);
            result.setSuccess(true);
            result.setData(dictTypeVO);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ViewerResult delete(@RequestBody JSONObject obj) {
        ViewerResult result = new ViewerResult();
        try {
            JSONArray ja = obj.getJSONArray("ids");
            String[] ids = new String[ja.size()];
            ja.toArray(ids);
            umsDictionaryTypeFacade.delete(ids);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ViewerResult update(@RequestBody UmsDictionaryType UmsDictionaryType) {
        ViewerResult result = new ViewerResult();
        try {
            com.rjsoft.uums.facade.dict.entity.UmsDictionaryType oldDic = umsDictionaryTypeFacade.getUmsDictionaryTypeById(UmsDictionaryType.getId());
            if (oldDic == null) {
                result.setSuccess(false);
                result.setErrMessage("该数据字典类型已不存在");
            } else {
                oldDic.setCode(UmsDictionaryType.getCode());
                oldDic.setDesc(UmsDictionaryType.getDesc());
                oldDic.setValue(UmsDictionaryType.getValue());
                UmsDictionaryType = umsDictionaryTypeFacade.update(oldDic);
                DictTypeVO dictTypeVO = new DictTypeVO();
                dictTypeVO.convertPOToVO(UmsDictionaryType);
                result.setSuccess(true);
                result.setData(dictTypeVO);
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/updAvailable", method = RequestMethod.POST)
    public ViewerResult updAvailable(@RequestBody JSONObject obj) {
        ViewerResult result = new ViewerResult();
        try {
            String id = obj.getString("id");
            short isAvailable = new Short(obj.getString("isAvailable"));
            UmsDictionaryType oldDic = umsDictionaryTypeFacade.getUmsDictionaryTypeById(id);
            oldDic.setIsAvailable(isAvailable);
            oldDic = umsDictionaryTypeFacade.update(oldDic);
            DictTypeVO dictTypeVO = new DictTypeVO();
            dictTypeVO.convertPOToVO(oldDic);
            result.setSuccess(true);
            result.setData(dictTypeVO);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
