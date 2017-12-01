package com.rjsoft.uums.api.controller.vo.org;

import com.rjsoft.common.vo.BaseVO;
import com.rjsoft.uums.facade.position.entity.UmsPosition;

public class PositionVO implements BaseVO {
	
	private String id;
	
	/**
	 * 岗位的名称
	 */
	private String name;
	
	@Override
	public void convertPOToVO(Object poObj) {
		
		if(poObj != null && poObj instanceof UmsPosition){
			UmsPosition position = (UmsPosition)poObj;
			this.id = position.getId();
			this.name = position.getName();
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