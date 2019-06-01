package com.cherkashyn.vitalii.components.sms.remoteservice.domain;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name="command")
public class Command {
	@Attribute
	private String name;

	public Command(String commandName){
		this.name=commandName;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Command [name=" + name + "]";
	}
	
	
}
