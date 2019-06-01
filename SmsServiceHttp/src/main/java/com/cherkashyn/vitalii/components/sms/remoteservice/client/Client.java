package com.cherkashyn.vitalii.components.sms.remoteservice.client;

import java.util.Arrays;
import java.util.Date;

import com.cherkashyn.vitalii.components.sms.remoteservice.client.command.QueryMessage;
import com.cherkashyn.vitalii.components.sms.remoteservice.client.command.RecvMessages;
import com.cherkashyn.vitalii.components.sms.remoteservice.client.command.SendMessage;
import com.cherkashyn.vitalii.components.sms.remoteservice.client.command.SmsLimit;
import com.cherkashyn.vitalii.components.sms.remoteservice.domain.Recipient;

/**
 * facade to operation as client of service with remote sms service
 */
public class Client {
	/**
	 * command for check limit of SMS for send 
	 * @return
	 */
	public static SmsLimit createSmsLimit(){
		return new SmsLimit();
	}
	
	
	/**
	 * command for get received messages 
	 */
	public static RecvMessages createRecvMessages(Date dateStart, Date dateEnd){
		return new RecvMessages(dateStart, dateEnd);
	}


	/**
	 * command for send message 
	 * @param id of client side ( server will ignore this value and just return it )
	 * @param recipientAddress
	 * @param messageText
	 * @return
	 */
	public static SendMessage createSendMessage(Long id, long recipientId, String recipientAddress, String messageText){
		return new SendMessage(Long.toString(id), Arrays.asList(new Recipient[]{new Recipient( recipientId, recipientAddress, messageText)}));
	}

	/**
	 * command for send message 
	 * @param id of client side ( server will ignore this value and just return it )
	 * @param recipientAddress
	 * @param messageText
	 * @return
	 */
	public static SendMessage createSendMessage(long recipientId, String recipientAddress, String messageText){
		return new SendMessage(null, Arrays.asList(new Recipient[]{new Recipient( recipientId, recipientAddress, messageText)}));
	}

	/**
	 * command for get status of sms messages 
	 * @param serverId - id of bulk 
	 * @return
	 */
	public static QueryMessage createQueryMessage(long serverId){
		return new QueryMessage(serverId);
	}
}
