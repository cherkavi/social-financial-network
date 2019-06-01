package com.cherkashyn.vitalii.components.sms.remoteservice.domain;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

@Root(name="message")
public class Message {
	@Attribute
	private String smsid;
	@Attribute
	String date; 
	@Attribute
	String addrfrom; 
	@Attribute
	String addrto;
	@Text
	String text;
	
	public String getSmsid() {
		return smsid;
	}
	public void setSmsid(String smsid) {
		this.smsid = smsid;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getAddrfrom() {
		return addrfrom;
	}
	public void setAddrfrom(String addrfrom) {
		this.addrfrom = addrfrom;
	}
	public String getAddrto() {
		return addrto;
	}
	public void setAddrto(String addrto) {
		this.addrto = addrto;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	@Override
	public String toString() {
		return "Message [smsid=" + smsid + ", date=" + date + ", addrfrom="
				+ addrfrom + ", addrto=" + addrto + ", text=" + text + "]";
	}
	
	
}
