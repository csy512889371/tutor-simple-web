package com.rjsoft.uums.api.controller.vo.dict;

import com.rjsoft.common.vo.BaseVO;
import com.rjsoft.uums.facade.dict.entity.UmsDictionaryType;

public class DictTypeVO implements BaseVO {
	
	private String id;
	
	private String code;
	
	private String value;
	
	private Short isAvailable;
	
	private String desc;
	
	@Override
	public void convertPOToVO(Object poObj) {
		
		if(poObj != null){
			UmsDictionaryType dictType = null;
			if(poObj instanceof UmsDictionaryType){
				dictType = (UmsDictionaryType)poObj;
			}
			if(dictType != null){
				this.id = dictType.getId();
				this.code = dictType.getCode();
				this.value = dictType.getValue();
				this.isAvailable = dictType.getIsAvailable();
				this.desc = dictType.getDesc();
			}
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Short getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Short isAvailable) {
		this.isAvailable = isAvailable;
	}
}