package com.cherkashyn.vitalii.components.sms.remoteservice.client.command;

import com.cherkashyn.vitalii.components.sms.remoteservice.domain.Auth;

/**
 * запрос остатка лимита SMS по учетной записи
 */
public class SmsLimit extends CommandRequest {

	public SmsLimit(){
		super();
	}
	public SmsLimit(Auth auth){
		super(auth);
	}
	

	private final static String NAME="getsmslimit";

	public String getRequestCommandName() {
		return NAME;
	}

}
