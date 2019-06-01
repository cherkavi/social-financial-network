package com.cherkashyn.vitalii.components.sms.remoteservice.client.command;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

import com.cherkashyn.vitalii.components.sms.remoteservice.client.Client;
import com.cherkashyn.vitalii.components.sms.remoteservice.domain.PacketResponse;
import com.cherkashyn.vitalii.components.sms.remoteservice.domain.Recipient.StatusResponse;
import com.cherkashyn.vitalii.components.sms.remoteservice.transport.HttpTransport;
import com.cherkashyn.vitalii.components.sms.remoteservice.transport.TransportException;


/**
 * this is integration test - command will request data from remote server <br /> 
 */
public class CommandTest {

	@Ignore
	@Test
	public void givenCommandSmsLimitExecuteIt() throws TransportException{
		PacketResponse packet=Client.createSmsLimit().execute(getTransport());
		Assert.assertTrue(packet.getResult().isValid());
		Assert.assertTrue(Integer.parseInt(packet.getResult().getSmslimit())>0);
		System.out.println(MessageFormat.format("Limit is: [{0}]", packet.getResult().getSmslimit()));
	}


	/**
	 * test for receive messages 
	 * @throws TransportException
	 */
	@Ignore
	@Test
	public void givenCommandRecvMessages() throws TransportException{
		Date date=getCurrentDate();
		PacketResponse packet=Client.createRecvMessages(getDateWithoutDay(date), date).execute(getTransport());
		Assert.assertTrue(packet.getResult().isValid());
		Assert.assertNotNull(packet.getResult().getMessages());
		System.out.println(MessageFormat.format("Recieve messages [size={0}]: {1}", packet.getResult().getMessages().size(), packet.getResult().getMessages()));
	}

	
	/**
	 * full cycle ( full integration ) test - send data, wait for delivery report 
	 * @throws TransportException
	 */
	@Ignore
	@Test
	public void givenRecipientSendMessageAndControlSending() throws TransportException{
		// given
		/** phone number for send */
		String phoneNumber="+380954671434"; //  "+380992473885"; // 
		/** id of client for send  */
		long recipientId=1L;
		/** text for send */
		String text="this is text message "+Integer.toString(new Random().nextInt(100000));
		/** id of package/bulk for sending */
		Long clientBulkId=(long)new Random().nextInt(100000);
		
		// send message
		PacketResponse packet=Client.createSendMessage(clientBulkId, recipientId, phoneNumber, text).execute(getTransport());
		System.out.println("message was sent");
		Assert.assertTrue(packet.getResult().getBulk().getRecipients().size()==1);
		Assert.assertTrue(packet.getResult().getBulk().getRecipients().get(0).getAddress().equals(phoneNumber));
		
		/** id of package from server side  */
		Long serverBulkId=packet.getResult().getBulk().getBulkId();
		// check message until status delivered will appear
		while(true){
			System.out.println("request server for status of bulk by id: "+serverBulkId);
			packet=Client.createQueryMessage(serverBulkId).execute(getTransport());
			Assert.assertNotNull(packet.getResult());
			Assert.assertNotNull(packet.getResult().getBulk());
			Assert.assertNotNull(packet.getResult().getBulk().getRecipients());
			Assert.assertTrue(packet.getResult().getBulk().getRecipients().size()==1);
			if(StatusResponse.DELIVERED.equals(packet.getResult().getBulk().getRecipients().get(0).getStatus())){
				System.out.println("status was caught : DELIVERED");
				return;
			}else{
				System.out.println(MessageFormat.format("next status: {0}   {1}", packet.getResult().getBulk().getRecipients().get(0).getStatus(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
				try {
					TimeUnit.SECONDS.sleep(1L);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	private Date getCurrentDate(){
		return new Date();
	}
	
	private Date getDateWithoutDay(Date date){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		return calendar.getTime();
	}
	
	
	private HttpTransport getTransport(){
		return new HttpTransport();
	}
}
