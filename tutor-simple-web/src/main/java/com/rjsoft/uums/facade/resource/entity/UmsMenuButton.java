package com.rjsoft.uums.facade.resource.entity;

import com.rjsoft.common.entity.UUIDEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="UMS_MENU_BUTTON")
public class UmsMenuButton extends UUIDEntity<String> {
	
	private static final long serialVersionUID = 1L;
	@Column(name="MENU_ID")
	private String menuId;
	@Column(name="BUTTON_ID")
	private String buttonId;
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getButtonId() {
		return buttonId;
	}
	public void setButtonId(String buttonId) {
		this.buttonId = buttonId;
	}

	
}
