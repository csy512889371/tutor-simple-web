package com.rjsoft.uums.facade.orgRole.exception;

import com.rjsoft.common.exception.BaseException;

public class OrgRoleException extends BaseException {

	private static final long serialVersionUID = 1L;

	public OrgRoleException(String code, Object[] args) {
        super("role", code, args, null);
    }

}
