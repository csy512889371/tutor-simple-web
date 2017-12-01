package com.rjsoft.uums.api.controller.vo.common;

import com.rjsoft.common.vo.BaseVO;
import com.rjsoft.uums.facade.app.entity.UmsApp;

public class AppForSelectVO implements BaseVO {
	
	private String id;
	
	/**
	 * 系统名称
	 */
	private String name;
	
	@Override
	public void convertPOToVO(Object poObj) {
		
		if(poObj != null && poObj instanceof UmsApp){
			UmsApp app = (UmsApp)poObj;
			this.id = app.getId();
			this.name = app.getName();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}