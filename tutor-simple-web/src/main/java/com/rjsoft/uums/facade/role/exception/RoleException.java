package com.rjsoft.uums.facade.role.exception;

import com.rjsoft.common.exception.BaseException;

public class RoleException extends BaseException {

	private static final long serialVersionUID = 1L;

	public RoleException(String code, Object[] args) {
        super("role", code, args, null);
    }

}
