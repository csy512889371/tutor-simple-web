package com.rjsoft.uums.facade.auth.domain;

import com.rjsoft.uums.facade.user.entity.UmsUser;

import java.io.Serializable;

public class Authorization implements Serializable {
	
	private static final long serialVersionUID = 8163063043281407701L;

	private int status;
	
	private UmsUser user;
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public UmsUser getUser() {
		return user;
	}

	public void setUser(UmsUser user) {
		this.user = user;
	}
}