package com.cherkashyn.vitalii.components.sms.remoteservice.domain;

public class SmsLimit {
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "SmsLimit [text=" + text + "]";
	}

	
}
