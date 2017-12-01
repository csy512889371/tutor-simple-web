package com.rjsoft.uums.facade.user.exception;

import com.rjsoft.common.exception.BaseException;

public class UserException extends BaseException {

	private static final long serialVersionUID = 1L;

	public UserException(String code, Object[] args) {
        super("user", code, args, null);
    }

}
