package com.cherkashyn.vitalii.components.sms.remoteservice.domain;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import com.cherkashyn.vitalii.components.sms.remoteservice.client.Constants;

@Root(name="auth")
public class Auth{
	@Attribute
	private String login;
	@Attribute
	private String password;
	
	public Auth(String serviceLogin, String servicePassword) {
		this.login=serviceLogin;
		this.password=servicePassword;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	private final static Auth DEFAULT=new Auth(Constants.SERVICE_LOGIN, Constants.SERVICE_PASSWORD);
	public static Auth getDefault() {
		return DEFAULT;
	}
	
	
	
}
