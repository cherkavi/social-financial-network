package com.cherkashyn.vitalii.components.sms.remoteservice.domain;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="packet")
public class PacketResponse {
	@Attribute
	private String version="1.0";
	@Element(name="result")
	private Result result;
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Result getResult() {
		return result;
	}
	public void setResult(Result result) {
		this.result = result;
	}
	
}
