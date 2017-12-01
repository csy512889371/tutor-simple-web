package com.rjsoft.uums.api.controller.log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rjsoft.common.model.search.Searchable;
import com.rjsoft.common.utils.DateUtil;
import com.rjsoft.common.utils.NetworkUtil;
import com.rjsoft.common.vo.PageVO;
import com.rjsoft.common.vo.ViewerResult;
import com.rjsoft.uums.api.controller.vo.log.LogVO;
import com.rjsoft.uums.facade.app.entity.UmsApp;
import com.rjsoft.uums.facade.app.service.UmsAppFacade;
import com.rjsoft.uums.facade.jwt.service.UmsJwtFacade;
import com.rjsoft.uums.facade.log.entity.UmsLog;
import com.rjsoft.uums.facade.log.enums.LogLevelEnum;
import com.rjsoft.uums.facade.log.enums.LogTypeEnum;
import com.rjsoft.uums.facade.log.enums.OpResultEnum;
import com.rjsoft.uums.facade.log.service.UmsLogFacade;
import com.rjsoft.uums.facade.resource.entity.UmsControllerResources;
import com.rjsoft.uums.facade.resource.entity.UmsMenuResources;
import com.rjsoft.uums.facade.resource.service.UmsControllerResFacade;
import com.rjsoft.uums.facade.resource.service.UmsMenuResFacade;
import com.rjsoft.uums.facade.user.service.UmsUserFacade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/log")
public class LogController {
	
	@Resource
	private UmsLogFacade umsLogFacade;
	
	@Resource
	private UmsJwtFacade umsJwtFacade;
	
	@Resource
	private UmsUserFacade umsUserFacade;
	
	@Resource
	private UmsAppFacade umsAppFacade;
	
	@Resource
	private UmsMenuResFacade umsMenuResFacade;
	
	@Resource
	private UmsControllerResFacade umsControllerResFacade;
	
	@RequestMapping(value="/findForPage", method= RequestMethod.POST)
	public @ResponseBody
    ViewerResult findForPage(HttpServletRequest request, @RequestBody JSONObject obj){
		ViewerResult result = new ViewerResult();
		Page<UmsLog> page = null;
		PageVO<LogVO> pageVO = null;
		try {
			String username = obj.getString("username");
			JSONArray rangeDate = obj.getJSONArray("rangeDate");
			LocalDateTime startOpTime = DateUtil.utilDateToLocalDateTime(rangeDate.getDate(0));
			LocalDateTime endOpTime = DateUtil.utilDateToLocalDateTime(rangeDate.getDate(1));
			int number = obj.getInteger("number");
			int size = obj.getInteger("size");
			Pageable pageable = PageRequest.of(number, size);
			Searchable searchable = Searchable.newSearchable();
			searchable.setPage(pageable);
			searchable.addSearchParam("username_like", username);
			searchable.addSearchParam("opTime_gte", startOpTime);
			searchable.addSearchParam("opTime_lte", endOpTime);
			searchable.addSort(Direction.DESC, "opTime");
			page = umsLogFacade.listPage(searchable);
			pageVO = new PageVO<>(page, LogVO.class);
			for(LogVO vo : pageVO.getPageData()){
				String sn = vo.getApp();
				if(sn != null) {
					UmsApp app = umsAppFacade.getBySn(sn);
					if(app != null){
						vo.setApp(app.getName());
					}
				}
			}
			result.setSuccess(true);
			result.setData(pageVO);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setErrMessage(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping(value="/saveAction", method= RequestMethod.POST)
	public @ResponseBody
    ViewerResult request(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject obj){
		return log(request, response, obj, LogTypeEnum.ACTION.getValue());
	}
	
	@RequestMapping(value="/saveInterface", method= RequestMethod.POST)
	public @ResponseBody
    ViewerResult response(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject obj){
		return log(request, response, obj, LogTypeEnum.INTERFACE.getValue());
	}
	
	private ViewerResult log(HttpServletRequest request, HttpServletResponse response, JSONObject obj, Short logType){
		ViewerResult result = new ViewerResult();
		try {
			JSONObject authorization = JSONObject.parseObject(request.getHeader("Authorization"));
			if(authorization == null || authorization.getString("appSn") == null){
				result.setSuccess(false);
				result.setErrMessage("非法请求");
			}else{
				String menuPath = obj.getString("menuPath");
				String frontEndAccessPath = obj.getString("accessPath");
				String opDesc = obj.getString("opDesc");
				Long execTime = obj.getLong("execTime");
				int status = obj.getInteger("status");
				long currentDateTime = (new Date()).getTime();
				long mOpTime = currentDateTime-execTime;
				LocalDateTime opTime = DateUtil.utilDateToLocalDateTime(new Date(mOpTime));
				Object appSnO = request.getAttribute("currentAppSn");
				Object usernameO = request.getAttribute("currentUsername");
				String appSn = appSnO == null ? null : (String)appSnO;
				String username = usernameO == null ? null : (String)usernameO;
				UmsLog umsLog = new UmsLog();
				Searchable searchable = Searchable.newSearchable();
				if(frontEndAccessPath == null){
					searchable.addSearchParam("controllerUrlMapping_eq", frontEndAccessPath);
					searchable.addSearchParam("menu.menuUrl_eq", menuPath);
					searchable.addSearchParam("application.sn_eq", appSn);
					searchable.addSearchParam("menu.application.sn_eq", appSn);
					List<UmsMenuResources> menus = umsMenuResFacade.listUmsMenuResources(searchable);
					if(menus.size()>0){
						UmsMenuResources menu = menus.get(0);
						umsLog.setOpResource(menu.getMenuName());
					}
				}else{
					searchable.addSearchParam("controllerUrlMapping_eq", frontEndAccessPath);
					searchable.addSearchParam("menu.menuUrl_eq", menuPath);
					searchable.addSearchParam("application.sn_eq", appSn);
					searchable.addSearchParam("menu.application.sn_eq", appSn);
					List<UmsControllerResources> controllers = umsControllerResFacade.listUmsControllerRes(searchable);
					if(controllers.size()>0){
						UmsControllerResources controller = controllers.get(0);
						umsLog.setOpResource(controller.getMenu().getMenuName());
					}
				}
				Short logLevel = null;
				Short opResult = null;
				if(status == 200){
					logLevel = LogLevelEnum.NORMAL.getValue();
					opResult = OpResultEnum.SUCCESS.getValue();
				}else{
					logLevel = LogLevelEnum.MAJOR.getValue();
					opResult = OpResultEnum.FAIl.getValue();
				}
				umsLog.setLogLevel(logLevel);
				umsLog.setAppSn(appSn);
				umsLog.setUsername(username);
				umsLog.setLogType(logType);
				umsLog.setFrontEndAccessPath(frontEndAccessPath);
				umsLog.setIp(NetworkUtil.getIpAddress(request));
				umsLog.setBrowser(NetworkUtil.getBrowser(request));
				umsLog.setOpDesc(opDesc);
				umsLog.setExecTime(execTime);
				umsLog.setOpResult(opResult);
				umsLog.setOpSystem(NetworkUtil.getOS(request));
				umsLog.setOpTime(opTime);
				umsLog.setOpType((short)0);
				umsLogFacade.save(umsLog);
				result.setSuccess(true);
			}
		} catch (Exception e) {
			result.setSuccess(false);
			result.setErrMessage("记入日志信息失败");
			e.printStackTrace();
		}
		return result;
	}
}
