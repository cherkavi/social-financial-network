package com.cherkashyn.vitalii.components.sms.remoteservice.client.command;

import java.util.List;

import com.cherkashyn.vitalii.components.sms.remoteservice.domain.Bulk;
import com.cherkashyn.vitalii.components.sms.remoteservice.domain.PacketRequest;
import com.cherkashyn.vitalii.components.sms.remoteservice.domain.Recipient;

public class SendMessage extends CommandRequest{

	private final static String NAME="sendmessage";
	private final Bulk bulk;

	public SendMessage(String clientId, List<Recipient> recipients){
		bulk=new Bulk(clientId, recipients);
	}

	public SendMessage(String clientId, String alphaname, List<Recipient> recipients){
		bulk=new Bulk(clientId, alphaname, recipients);
	}
	
	@Override
	protected PacketRequest fillAdditionalFields(PacketRequest packetRequest) {
		packetRequest.setBulk(bulk);
		return packetRequest;
	}
	
	
	@Override
	protected String getRequestCommandName() {
		return NAME;
	}

	
}
