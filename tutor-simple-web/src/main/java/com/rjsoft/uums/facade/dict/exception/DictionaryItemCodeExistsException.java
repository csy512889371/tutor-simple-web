package com.rjsoft.uums.facade.dict.exception;


public class DictionaryItemCodeExistsException extends DictionaryException {

	private static final long serialVersionUID = 1L;

	public DictionaryItemCodeExistsException() {
        super("dict.item.code.exists", null);
    }
}
