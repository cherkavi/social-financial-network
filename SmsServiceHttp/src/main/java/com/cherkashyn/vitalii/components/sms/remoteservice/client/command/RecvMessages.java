package com.cherkashyn.vitalii.components.sms.remoteservice.client.command;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.cherkashyn.vitalii.components.sms.remoteservice.domain.Auth;
import com.cherkashyn.vitalii.components.sms.remoteservice.domain.PacketRequest;

/**
 * Запрос принятых SMS сообщений
 */
public class RecvMessages extends CommandRequest{

	private final static String NAME="getrecvmessages";
	private Date	startDate;
	private Date	endDate;
	
	public RecvMessages(Date startDate, Date endDate){
		this(null, startDate, endDate);
	}
	public RecvMessages(Auth auth, Date startDate, Date endDate){
		super(auth);
		this.startDate=startDate;
		this.endDate=endDate;
	}
	
	@Override
	protected PacketRequest fillAdditionalFields(PacketRequest packetRequest) {
		packetRequest.setStartDate(convert(startDate));
		packetRequest.setEndDate(convert(endDate));
		return packetRequest;
	}
	
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	String convert(Date date){
		return sdf.format(date);
	}
	
	@Override
	protected String getRequestCommandName() {
		return NAME;
	}

}
