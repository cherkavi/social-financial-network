package ua.com.nmtg.private_office.web_service.generator.code_description.elements;

import org.apache.commons.lang.StringUtils;

public class MethodReturnValue {
	private String type;
	private final static String VOID="void";
	
	public MethodReturnValue(String type){
		
		this.type=StringUtils.trimToNull(type);
		if(this.type==null){
			this.type=VOID;
		}
	}

	public String getType(){
		return this.type;
	}

	@Override
	public String toString() {
		return this.type;
	}
}
