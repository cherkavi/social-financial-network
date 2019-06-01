package com.cherkashyn.vitalii.components.sms.remoteservice.client.command;

import com.cherkashyn.vitalii.components.sms.remoteservice.domain.Auth;
import com.cherkashyn.vitalii.components.sms.remoteservice.domain.Bulk;
import com.cherkashyn.vitalii.components.sms.remoteservice.domain.PacketRequest;

/**
 * request the status of SMS messages
 */
public class QueryMessage extends CommandRequest{
	private final static String NAME="querymessage";
	private long bulkId;
	
	/**
	 * server id which was given in step : {@link SendMessage}
	 * @param bulkId
	 */
	public QueryMessage(long bulkId){
		super();
		this.bulkId=bulkId;
	}

	public QueryMessage(Auth auth, long bulkId){
		super(auth);
		this.bulkId=bulkId;
	}
	
	@Override
	protected String getRequestCommandName() {
		return NAME;
	}
	
	@Override
	protected PacketRequest fillAdditionalFields(PacketRequest packetRequest) {
		packetRequest.setBulk(new Bulk(this.bulkId));
		return packetRequest;
	}

}
