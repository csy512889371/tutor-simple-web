package com.rjsoft.uums.api.controller.dict;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rjsoft.common.model.search.Searchable;
import com.rjsoft.common.vo.PageVO;
import com.rjsoft.common.vo.ViewerResult;
import com.rjsoft.uums.api.controller.vo.dict.DictItemVO;
import com.rjsoft.uums.facade.dict.entity.UmsDictionaryItem;
import com.rjsoft.uums.facade.dict.entity.UmsDictionaryType;
import com.rjsoft.uums.facade.dict.service.UmsDictionaryItemFacade;
import com.rjsoft.uums.facade.dict.service.UmsDictionaryTypeFacade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/dict")
public class DictController {

    @Resource
    private UmsDictionaryItemFacade umsDictionaryItemFacade;

    @Resource
    private UmsDictionaryTypeFacade umsDictionaryTypeFacade;

    @RequestMapping(value = "/findForPage", method = RequestMethod.POST)
    public ViewerResult findForPage(HttpServletRequest request, @RequestBody JSONObject obj) {
        ViewerResult result = new ViewerResult();
        Page<UmsDictionaryItem> page = null;
        PageVO<DictItemVO> pageVO = null;
        try {
            String name = obj.getString("name");
            String typeId = obj.getString("typeId");
            int number = obj.getInteger("number");
            int size = obj.getInteger("size");
            Pageable pageable = PageRequest.of(number, size);
            Searchable searchable = Searchable.newSearchable();
            searchable.setPage(pageable);
            searchable.addSearchParam("value_like", name);
            searchable.addSearchParam("type.id_eq", typeId);
            page = umsDictionaryItemFacade.listPage(searchable);
            pageVO = new PageVO<>(page, DictItemVO.class);
            result.setSuccess(true);
            result.setData(pageVO);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ViewerResult add(@RequestBody UmsDictionaryItem umsDictionaryItem) {
        ViewerResult result = new ViewerResult();
        try {
            umsDictionaryItem = umsDictionaryItemFacade.create(umsDictionaryItem);
            DictItemVO dictItemVO = new DictItemVO();
            dictItemVO.convertPOToVO(umsDictionaryItem);
            result.setSuccess(true);
            result.setData(dictItemVO);
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
            umsDictionaryItemFacade.delete(ids);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ViewerResult update(@RequestBody UmsDictionaryItem umsDictionaryItem) {
        ViewerResult result = new ViewerResult();
        try {
            UmsDictionaryItem oldDic = umsDictionaryItemFacade.getUmsDictionaryItemById(umsDictionaryItem.getId());
            if (oldDic == null) {
                result.setSuccess(false);
                result.setErrMessage("该数据字典已不存在");
            } else {
                UmsDictionaryType type = umsDictionaryItem.getType();
                if (type == null || type.getId() == null) {
                    oldDic.setType(null);
                } else {
                    oldDic.setType(type);
                }
                oldDic.setCode(umsDictionaryItem.getCode());
                oldDic.setSerial(umsDictionaryItem.getSerial());
                oldDic.setValue(umsDictionaryItem.getValue());
                umsDictionaryItem = umsDictionaryItemFacade.update(oldDic);
                DictItemVO dictItemVO = new DictItemVO();
                dictItemVO.convertPOToVO(umsDictionaryItem);
                result.setSuccess(true);
                result.setData(dictItemVO);
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
            UmsDictionaryItem oldDic = umsDictionaryItemFacade.getUmsDictionaryItemById(id);
            oldDic.setIsAvailable(isAvailable);
            oldDic = umsDictionaryItemFacade.update(oldDic);
            DictItemVO dictItemVO = new DictItemVO();
            dictItemVO.convertPOToVO(oldDic);
            result.setSuccess(true);
            result.setData(dictItemVO);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
