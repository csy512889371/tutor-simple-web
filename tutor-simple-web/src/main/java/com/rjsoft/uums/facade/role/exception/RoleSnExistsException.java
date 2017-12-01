package com.rjsoft.uums.facade.role.exception;


public class RoleSnExistsException extends RoleException {

	private static final long serialVersionUID = 1L;

	public RoleSnExistsException() {
        super("role.sn.exists", null);
    }
}
