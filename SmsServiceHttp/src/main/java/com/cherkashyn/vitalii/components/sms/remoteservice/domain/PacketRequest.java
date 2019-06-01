package com.cherkashyn.vitalii.components.sms.remoteservice.domain;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="packet")
public class PacketRequest {
	@Attribute
	private String version="1.0";
	@Element(name="auth")
	private Auth authentification;
	@Element(name="command")
	private Command command;
	
	/** {@link RecvMessages} */
	@Element(name="startdate", required=false)
	private String startDate;
	/** {@link RecvMessages} */
	@Element(name="enddate", required=false)
	private String endDate;
	
	/** {@link QueryMessage} */
	@Element(name="bulk", required=false)
	private Bulk bulk;
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Auth getAuthentification() {
		return authentification;
	}
	public void setAuthentification(Auth authentification) {
		this.authentification = authentification;
	}
	public Command getCommand() {
		return command;
	}
	public void setCommand(Command command) {
		this.command = command;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Bulk getBulk() {
		return bulk;
	}
	public void setBulk(Bulk bulk) {
		this.bulk = bulk;
	}
	
}
