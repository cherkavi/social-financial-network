package com.cherkashyn.vitalii.components.sms.remoteservice.client.command;

import java.io.StringWriter;

import org.simpleframework.xml.core.Persister;

import com.cherkashyn.vitalii.components.sms.remoteservice.domain.Auth;
import com.cherkashyn.vitalii.components.sms.remoteservice.domain.Command;
import com.cherkashyn.vitalii.components.sms.remoteservice.domain.PacketRequest;
import com.cherkashyn.vitalii.components.sms.remoteservice.domain.PacketResponse;
import com.cherkashyn.vitalii.components.sms.remoteservice.transport.HttpTransport;
import com.cherkashyn.vitalii.components.sms.remoteservice.transport.TransportException;

abstract class CommandRequest {
	private final static String XML_HEADER="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";

	protected Auth authentication;
	
	protected CommandRequest(){
		this(null);
	}

	protected CommandRequest(Auth auth){
		if(auth==null){
			this.authentication=Auth.getDefault();
		}else{
			this.authentication=auth;
		}
	}

	/**
	 * execute request to remote server
	 * @param transport - transport for send data to remote host
	 * @return return result as object
	 * @throws TransportException 
	 */
	public PacketResponse execute(HttpTransport transport) throws TransportException {
		Persister persister=new Persister();
		String response=transport.sendData(buildRequestBody(persister));
		return parseResponse(persister, response);
	}

	/**
	 * create {@link PacketRequest} and set Authentication information and command name 
	 * @return
	 */
	protected PacketRequest createAndFillMandatoryFieldsPacketRequest(){
		PacketRequest packet=new PacketRequest();
		packet.setAuthentification(authentication);
		packet.setCommand(new Command(getRequestCommandName()));
		return packet;
	}
	
	/**
	 * parse response from remote server 
	 * @param persister
	 * @param response
	 * @return
	 * @throws TransportException
	 */
	PacketResponse parseResponse(Persister persister, String response) throws TransportException{
		try {
			return persister.read(PacketResponse.class, response);
		} catch (Exception e) {
			throw new TransportException("can't read server response ", e);
		}
	}


	/**
	 * create {@link PakcetRequst}, fill additional fields ( protected ), create XML String  
	 * @param persister
	 * @return
	 * @throws TransportException
	 */
	String buildRequestBody(Persister persister) throws TransportException{
		PacketRequest packet=this.createAndFillMandatoryFieldsPacketRequest();
		fillAdditionalFields(packet);
		StringWriter writer=new StringWriter();
		writer.write(XML_HEADER);
		try {
			persister.write(packet, writer);
		} catch (Exception e) {
			throw new TransportException("can't transform object to XML ", e);
		}
		return writer.toString();
	}
	
	/**
	 * write additional fields to {@link PacketRequest}
	 * @param packetRequest
	 * @return
	 */
	protected PacketRequest fillAdditionalFields(PacketRequest packetRequest){
		return packetRequest;
	}

	/**
	 * @return name of command (as leaf of xml &ltcommand name="name of command" &gt )
	 */
	protected abstract String getRequestCommandName();
	
	
	
}
