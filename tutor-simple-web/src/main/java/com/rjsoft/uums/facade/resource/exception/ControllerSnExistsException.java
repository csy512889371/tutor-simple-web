package com.rjsoft.uums.facade.resource.exception;


public class ControllerSnExistsException extends ResourceException {

	private static final long serialVersionUID = 1L;
	
	public ControllerSnExistsException() {
        super("resource.controller.sn.exists", null);
    }
}
