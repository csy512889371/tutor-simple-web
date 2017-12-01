package com.rjsoft.uums.api.controller.vo.common;

import com.rjsoft.common.vo.BaseVO;
import com.rjsoft.uums.api.util.tree.service.ITreeNode;
import com.rjsoft.uums.facade.resource.entity.UmsMenuResources;

public class MenuResourceForAuthVO implements BaseVO,ITreeNode {
	
	private String id;
	
	private String name;

	private String icon;
	
	private String parentId;
	
	private Integer orderNum;
	
	@Override
	public void convertPOToVO(Object poObj) {
		
		if(poObj != null && poObj instanceof UmsMenuResources){
			UmsMenuResources mr = (UmsMenuResources)poObj;
			this.id = mr.getId();
			this.name = mr.getMenuName();
			this.icon = mr.getMenuIcon();
			UmsMenuResources parent = mr.getParent();
			if(parent != null){
				this.parentId = parent.getId();
			}
			this.orderNum = mr.getMenuOrder();
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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	@Override
	public String getNodeId() {
		return this.id;
	}

	@Override
	public String getNodeName() {
		return this.name;
	}

	@Override
	public String getNodeParentId() {
		return this.parentId;
	}

	@Override
	public Integer getOrderNum() {
		return this.orderNum;
	}
}