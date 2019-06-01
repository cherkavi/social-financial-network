package com.cherkashyn.vitalii.components.sms.remoteservice.domain;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="result")
public class Result {
	@Attribute(name="type")
	private String type;
	/** {@link SmsLimit}*/
	@Element(name="smslimit", required=false)
	private String smslimit;
	
	/** {@link RecvMessages}*/
	@ElementList(name="messages", inline=false, required=false)
	private List<Message> messages;
	
	/** {@link QueryMessage}*/
	@Element(name="bulk", required=false)
	private Bulk bulk;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public boolean isValid(){
		return Case.successful.getValue().equals(this.type);
	}
	
	public static enum Case{
		successful("00", "запрос успешно обработан"),
		error_parameters("01","неверные значения параметров или недостаточное кол-во параметров"),
		error_db_connection("02","ошибка соединения с сервером баз данных"),
		error_db_not_found("03","не найдена база данных"),
		error_autorization("04","сбой процедуры авторизации"),
		error_login_password("05","неверное имя пользователя или пароль"),
		error_execute_operation("06","ошибка выполнения операции");
		
		private final String value;
		private final int intValue;
		private final String message;
		
		Case(String value, String message){
			this.value=value;
			this.message=message;
			this.intValue=Integer.parseInt(this.value);
		}
		
		public static String getMessage(String value){
			for(Case eachCase:Case.values()){
				if(eachCase.value.equals(value)){
					return eachCase.message;
				}
			}
			return null;
		}
		
		public String getValue(){
			return this.value;
		}
		public String getMessage(){
			return this.message;
		}
		
		public Case getCase(String caseValue){
			if(caseValue==null){
				return null;
			}
			String value=caseValue.trim();
			for( Case eachCase : Case.values()){
				if(eachCase.getValue().equals(value)){
					return eachCase;
				}
			}
			int intCaseValue=Integer.parseInt(value);
			for( Case eachCase : Case.values()){
				if(eachCase.intValue==intCaseValue){
					return eachCase;
				}
			}
			return null;
		}
	}

	public String getSmslimit() {
		return smslimit;
	}

	public void setSmslimit(String smslimit) {
		this.smslimit = smslimit;
	}


	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public Bulk getBulk() {
		return bulk;
	}

	public void setBulk(Bulk bulk) {
		this.bulk = bulk;
	}
	
}
