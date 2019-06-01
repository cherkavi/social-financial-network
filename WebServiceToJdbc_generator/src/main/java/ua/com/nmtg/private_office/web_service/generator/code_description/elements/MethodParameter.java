package ua.com.nmtg.private_office.web_service.generator.code_description.elements;

import org.apache.commons.lang.StringUtils;

public class MethodParameter {
	private String type;
	private String name;

	public MethodParameter(String type){
		this(type, null);
	}

	public MethodParameter(String type, String name){
		this.type=StringUtils.trimToNull(type);
		if(this.type==null){
			throw new IllegalArgumentException("check parameter type ");
		}
		this.name=StringUtils.trimToNull(name);
	}

	public String getType(){
		return this.type;
	}
	
	public String getName(){
		return this.name;
	}
}
