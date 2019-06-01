package com.cherkashyn.vitalii.components.sms.remoteservice.client.command;

import java.text.MessageFormat;
import java.text.ParseException;

import junit.framework.Assert;

import org.junit.Test;
import org.simpleframework.xml.core.Persister;

import com.cherkashyn.vitalii.components.sms.remoteservice.client.Constants;
import com.cherkashyn.vitalii.components.sms.remoteservice.domain.PacketResponse;
import com.cherkashyn.vitalii.components.sms.remoteservice.domain.Recipient;
import com.cherkashyn.vitalii.components.sms.remoteservice.transport.TransportException;

public class QueryMessageTest {
	
	@Test
	public void givenCommandTestXml() throws TransportException, ParseException{
		QueryMessage command=new QueryMessage(5L);
		String xml=command.buildRequestBody(getPersister());
		Assert.assertTrue(xml.contains("<packet version="));
		Assert.assertTrue(xml.contains("</packet>"));
		Assert.assertTrue(xml.contains(MessageFormat.format("<auth login=\"{0}\" password=\"{1}\"", Constants.SERVICE_LOGIN, Constants.SERVICE_PASSWORD)));
		Assert.assertTrue(xml.contains("<command name=\"querymessage\""));
		Assert.assertTrue(xml.contains("<bulk bulkid=\"5\""));
	}


	@Test
	public void givenResponseParseIt() throws TransportException{
		SmsLimit command=new SmsLimit();
		PacketResponse response=command.parseResponse(getPersister(), getResponse());
		Assert.assertEquals("1.0", response.getVersion());
		Assert.assertTrue(response.getResult().isValid());
		Assert.assertNotNull(response.getResult().getBulk());
		Assert.assertNotNull(response.getResult().getBulk().getRecipients());
		Assert.assertTrue(response.getResult().getBulk().getRecipients().size()==3);
		// 0
		Recipient recipient=response.getResult().getBulk().getRecipients().get(0);
		Assert.assertNotNull(recipient);
		Assert.assertEquals("001",recipient.getId());
		Assert.assertEquals("190",recipient.getSmsid());
		Assert.assertEquals(Recipient.StatusResponse.DELIVERED,recipient.getStatus());
		// 1
		recipient=response.getResult().getBulk().getRecipients().get(1);
		Assert.assertNotNull(recipient);
		Assert.assertEquals("002",recipient.getId());
		Assert.assertEquals("191",recipient.getSmsid());
		Assert.assertEquals(Recipient.StatusResponse.FAILED,recipient.getStatus());
		// 2
		recipient=response.getResult().getBulk().getRecipients().get(2);
		Assert.assertNotNull(recipient);
		Assert.assertEquals("002",recipient.getId());
		Assert.assertEquals("192",recipient.getSmsid());
		Assert.assertEquals(Recipient.StatusResponse.EXPIRED,recipient.getStatus());
		
	}

	

	private String getResponse(){
		StringBuilder returnValue=new StringBuilder();
		returnValue.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("\n");
		returnValue.append("<packet version=\"1.0\">").append("\n");
		returnValue.append("<result type=\"00\">").append("\n");
		returnValue.append("	<bulk bulkid=\"5\">").append("\n");
		returnValue.append("		<recipients>").append("\n");
		returnValue.append("			<recipient id=\"001\" smsid=\"190\" status=\"DELIVERED\"/>").append("\n");
		returnValue.append("			<recipient id=\"002\" smsid=\"191\" status=\"FAILED\"/>").append("\n");
		returnValue.append("			<recipient id=\"002\" smsid=\"192\" status=\"EXPIRED\"/>").append("\n");
		returnValue.append("		</recipients>").append("\n");
		returnValue.append("	</bulk>").append("\n");
		returnValue.append("</result>").append("\n");
		returnValue.append("</packet>").append("\n");
		
		return returnValue.toString();
	}
	
	
	private Persister getPersister() {
		return new Persister();	
	}
}
