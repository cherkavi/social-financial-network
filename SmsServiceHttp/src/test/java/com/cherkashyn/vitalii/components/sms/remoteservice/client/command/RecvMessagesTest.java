package com.cherkashyn.vitalii.components.sms.remoteservice.client.command;

import java.text.MessageFormat;
import java.text.ParseException;

import junit.framework.Assert;

import org.junit.Test;
import org.simpleframework.xml.core.Persister;

import com.cherkashyn.vitalii.components.sms.remoteservice.client.Constants;
import com.cherkashyn.vitalii.components.sms.remoteservice.domain.Message;
import com.cherkashyn.vitalii.components.sms.remoteservice.domain.PacketResponse;
import com.cherkashyn.vitalii.components.sms.remoteservice.transport.TransportException;

public class RecvMessagesTest {
	
	private String startDateString="2013-11-14 04:32:30";
	private String endDateString="2013-11-16 04:32:30";
	
	@Test
	public void givenCommandTestXml() throws TransportException, ParseException{
		RecvMessages command=new RecvMessages(new RecvMessages(null, null).sdf.parse(startDateString), new RecvMessages(null, null).sdf.parse(endDateString));
		String xml=command.buildRequestBody(getPersister());
		Assert.assertTrue(xml.contains("<packet version="));
		Assert.assertTrue(xml.contains("</packet>"));
		Assert.assertTrue(xml.contains(MessageFormat.format("<auth login=\"{0}\" password=\"{1}\"", Constants.SERVICE_LOGIN, Constants.SERVICE_PASSWORD)));
		Assert.assertTrue(xml.contains("<command name=\"getrecvmessages\""));
		Assert.assertTrue(xml.contains("<startdate>"+startDateString+"</startdate>"));
		Assert.assertTrue(xml.contains("<enddate>"+endDateString+"</enddate>"));
	}


	@Test
	public void givenResponseParseIt() throws TransportException{
		SmsLimit command=new SmsLimit();
		PacketResponse response=command.parseResponse(getPersister(), getResponse());
		Assert.assertEquals("1.0", response.getVersion());
		Assert.assertTrue(response.getResult().isValid());
		Assert.assertNotNull(response.getResult().getMessages());
		Assert.assertNotNull(response.getResult().getMessages());
		Assert.assertTrue(response.getResult().getMessages().size()==2);
		Message message1=response.getResult().getMessages().get(0);
		Assert.assertNotNull(message1);
		Assert.assertEquals("1",message1.getSmsid());
		Assert.assertEquals("2009-09-02 11:16:10",message1.getDate());
		Assert.assertEquals("380667772611",message1.getAddrfrom());
		Assert.assertEquals("380977770511",message1.getAddrto());
		Assert.assertEquals("test",message1.getText());
		
		Message message2=response.getResult().getMessages().get(1);
		Assert.assertNotNull(message2);
		Assert.assertEquals("2",message2.getSmsid());
		Assert.assertEquals("2009-09-14 12:30:40",message2.getDate());
		Assert.assertEquals("380507777911",message2.getAddrfrom());
		Assert.assertEquals("380977770511",message2.getAddrto());
		Assert.assertEquals("test",message2.getText());
	}

	

	private String getResponse(){
		StringBuilder returnValue=new StringBuilder();
		returnValue.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("\n");
		returnValue.append("<packet version=\"1.0\">").append("\n");
		returnValue.append("<result type=\"00\">").append("\n");
		returnValue.append("<messages>").append("\n");
		returnValue.append("<message smsid=\"1\" date=\"2009-09-02 11:16:10\" addrfrom=\"380667772611\" addrto=\"380977770511\">test</message>").append("\n");
		returnValue.append("<message smsid=\"2\" date=\"2009-09-14 12:30:40\" addrfrom=\"380507777911\" addrto=\"380977770511\">test</message>").append("\n");
		returnValue.append("</messages>").append("\n");
		returnValue.append("</result>").append("\n");
		returnValue.append("</packet>").append("\n");
		return returnValue.toString();
	}
	
	
	private Persister getPersister() {
		return new Persister();	
	}
}
