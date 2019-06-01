package com.cherkashyn.vitalii.components.sms.remoteservice.client.command;

import java.text.MessageFormat;

import junit.framework.Assert;

import org.junit.Test;
import org.simpleframework.xml.core.Persister;

import com.cherkashyn.vitalii.components.sms.remoteservice.client.Constants;
import com.cherkashyn.vitalii.components.sms.remoteservice.domain.PacketResponse;
import com.cherkashyn.vitalii.components.sms.remoteservice.transport.TransportException;

public class SmsLimitTest {

	@Test
	public void givenCommandTestXml() throws TransportException{
		SmsLimit command=new SmsLimit();
		String xml=command.buildRequestBody(getPersister());
		Assert.assertTrue(xml.contains("<packet version="));
		Assert.assertTrue(xml.contains("</packet>"));
		Assert.assertTrue(xml.contains(MessageFormat.format("<auth login=\"{0}\" password=\"{1}\"", Constants.SERVICE_LOGIN, Constants.SERVICE_PASSWORD)));
		Assert.assertTrue(xml.contains("<command name=\"getsmslimit\""));
	}

	@Test
	public void givenResponseParseIt() throws TransportException{
		SmsLimit command=new SmsLimit();
		PacketResponse response=command.parseResponse(getPersister(), getResponse());
		Assert.assertEquals("1.0", response.getVersion());
		Assert.assertTrue(response.getResult().isValid());
		Assert.assertEquals("17604", response.getResult().getSmslimit());
	}

	
	
	private String getResponse(){
		StringBuilder returnValue=new StringBuilder();
		returnValue.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("\n");
		returnValue.append("<packet version=\"1.0\">").append("\n");
		returnValue.append("	<result type=\"00\">").append("\n");
		returnValue.append("		<smslimit>17604</smslimit>").append("\n");
		returnValue.append("	</result>").append("\n");
		returnValue.append("</packet>").append("\n");
		return returnValue.toString();
	}
	
	
	private Persister getPersister() {
		return new Persister();	
	}
}
