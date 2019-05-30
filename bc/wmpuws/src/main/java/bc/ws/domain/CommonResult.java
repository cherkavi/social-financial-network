package bc.ws.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CommonResult {
	private String warnMessage;
	@XmlElement(name="resultCode")
	private String code;

	
	public CommonResult(){
	}

	public CommonResult(Integer returnCode, String message){
		this.code=parseString(returnCode);
		this.warnMessage=message;
	}

	public CommonResult(String returnCode, String message){
		this.code=returnCode;
		this.warnMessage=message;
	}
	
	private final static String parseString(Integer value){
		return value==null?null:value.toString();
	}

	private final static Integer parseInteger(String value){
		if(value==null){
			return null;
		}
		try{
			return Integer.parseInt(value);
		}catch(NumberFormatException ne){
			return null;
		}
	}
	
	public String getWarnMessage() {
		return warnMessage;
	}
	public void setWarnMessage(String warnMessage) {
		this.warnMessage = warnMessage;
	}
	public Integer getCode() {
		return parseInteger(code);
	}
	public String getCodeAsString(){
		return this.code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setCode(Integer code){
		this.code=parseString(code);
	}
	
}
