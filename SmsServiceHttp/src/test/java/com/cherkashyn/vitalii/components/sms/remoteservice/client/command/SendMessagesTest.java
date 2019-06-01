package com.cherkashyn.vitalii.components.sms.remoteservice.client.command;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.simpleframework.xml.core.Persister;

import com.cherkashyn.vitalii.components.sms.remoteservice.client.Constants;
import com.cherkashyn.vitalii.components.sms.remoteservice.domain.PacketResponse;
import com.cherkashyn.vitalii.components.sms.remoteservice.domain.Recipient;
import com.cherkashyn.vitalii.components.sms.remoteservice.transport.TransportException;

public class SendMessagesTest {
	
	private String id="12345";
	@Test
	public void givenCommandTestXml() throws TransportException, ParseException{
		List<Recipient> recipients=getRecipients();
		SendMessage command=new SendMessage(id, recipients);
		String xml=command.buildRequestBody(getPersister());
		Assert.assertTrue(xml.contains("<packet version="));
		Assert.assertTrue(xml.contains("</packet>"));
		Assert.assertTrue(xml.contains(MessageFormat.format("<auth login=\"{0}\" password=\"{1}\"", Constants.SERVICE_LOGIN, Constants.SERVICE_PASSWORD)));
		Assert.assertTrue(xml.contains("<command name=\"sendmessage\""));
		Assert.assertTrue(xml.contains("<recipients>"));
		Assert.assertTrue(xml.contains(MessageFormat.format("<recipient id=\"{0}\" address=\"{1}\">{2}", RECIPIENT_ID1,RECIPIENT_ADDRESS1,RECIPIENT_TEXT1)));
		Assert.assertTrue(xml.contains(MessageFormat.format("<recipient id=\"{0}\" address=\"{1}\">{2}", RECIPIENT_ID2,RECIPIENT_ADDRESS2,RECIPIENT_TEXT2)));
		Assert.assertTrue(xml.contains("</recipients>"));
	}


	@Test
	public void givenResponseParseIt() throws TransportException{
		SendMessage command=new SendMessage(null, null);
		PacketResponse response=command.parseResponse(getPersister(), getResponse());
		Assert.assertEquals("1.0", response.getVersion());
		Assert.assertTrue(response.getResult().isValid());
		Assert.assertNotNull(response.getResult().getBulk());
		Assert.assertNotNull(response.getResult().getBulk().getRecipients());
		Assert.assertTrue(response.getResult().getBulk().getRecipients().size()==3);

		Recipient recipient=response.getResult().getBulk().getRecipients().get(0);
		Assert.assertNotNull(recipient);
		Assert.assertNotNull(recipient.getId());
		Assert.assertEquals("+380667772611",recipient.getAddress());
		Assert.assertEquals("190",recipient.getSmsid());
		Assert.assertEquals(1L, recipient.getId());
		
		recipient=response.getResult().getBulk().getRecipients().get(1);
		Assert.assertNotNull(recipient);
		Assert.assertNotNull(recipient.getId());
		Assert.assertEquals("+380977770511",recipient.getAddress());
		Assert.assertEquals("191",recipient.getSmsid());
		Assert.assertEquals(2L, recipient.getId());
		
		recipient=response.getResult().getBulk().getRecipients().get(2);
		Assert.assertNotNull(recipient);
		Assert.assertNotNull(recipient.getId());
		Assert.assertEquals("+380507777911",recipient.getAddress());
		Assert.assertEquals("192",recipient.getSmsid());
		Assert.assertEquals(3L, recipient.getId());
		
	}

	private final static long RECIPIENT_ID1=100L;
	private final static String RECIPIENT_ADDRESS1="+380954671434";
	private final static String RECIPIENT_TEXT1="this is text";

	private final static long RECIPIENT_ID2=101L;
	private final static String RECIPIENT_ADDRESS2="+380950000000";
	private final static String RECIPIENT_TEXT2="this is text";

	private List<Recipient> getRecipients() {
		List<Recipient> returnValue=new ArrayList<Recipient>();
		returnValue.add(new Recipient(RECIPIENT_ID1, RECIPIENT_ADDRESS1, RECIPIENT_TEXT1));
		returnValue.add(new Recipient(RECIPIENT_ID2, RECIPIENT_ADDRESS2, RECIPIENT_TEXT2));
		return returnValue;
	}


	private String getResponse(){
		StringBuilder returnValue=new StringBuilder();
		returnValue.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("\n");
		returnValue.append("<packet version=\"1.0\">").append("\n");
		returnValue.append("	<result type=\"00\">").append("\n");
		returnValue.append("		<bulk id=\"111\" bulkid=\"5\">").append("\n");
		returnValue.append("			<recipients>").append("\n");
		returnValue.append("				<recipient id=\"001\" address=\"+380667772611\" smsid=\"190\"/>").append("\n");
		returnValue.append("				<recipient id=\"002\" address=\"+380977770511\" smsid=\"191\"/>").append("\n");
		returnValue.append("				<recipient id=\"003\" address=\"+380507777911\" smsid=\"192\"/>").append("\n");
		returnValue.append("			</recipients>").append("\n");
		returnValue.append("		</bulk>").append("\n");
		returnValue.append("	</result>").append("\n");
		returnValue.append("</packet>").append("\n");
		return returnValue.toString();
	}
	
	
	private Persister getPersister() {
		return new Persister();	
	}
}
