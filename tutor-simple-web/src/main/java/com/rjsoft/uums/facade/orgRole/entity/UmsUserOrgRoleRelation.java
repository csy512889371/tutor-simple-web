package com.rjsoft.uums.facade.orgRole.entity;

import com.rjsoft.common.entity.UUIDEntity;
import com.rjsoft.uums.facade.user.entity.UmsUser;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * User:cxtww
 * Date:2016年11月8日 下午3:07:39
 * Version:1.0
 */
@Entity
@Table(name = "UMS_USER_ORG_ROLE_RELATION")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UmsUserOrgRoleRelation extends UUIDEntity<String> {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	@JoinColumn(name="ROLE_ID", referencedColumnName="ID")
	private UmsOrgRole role;
	
	@ManyToOne
	@JoinColumn(name="USER_ID", referencedColumnName="ID")
	private UmsUser user;

	public UmsOrgRole getRole() {
		return role;
	}

	public void setRole(UmsOrgRole role) {
		this.role = role;
	}

	public UmsUser getUser() {
		return user;
	}

	public void setUser(UmsUser user) {
		this.user = user;
	}
}
