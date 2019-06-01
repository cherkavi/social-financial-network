package com.cherkashyn.vitalii.components.sms.remoteservice.domain;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import com.cherkashyn.vitalii.components.sms.remoteservice.client.Constants;

@Root(name="bulk")
public class Bulk {
	@Attribute(name="bulkid", required=false)
	private Long bulkId;
	@Attribute(name="id", required=false)
	private String id;
	@Attribute(name="type", required=false)
	private String type;
	@Attribute(name="name", required=false)
	private String name;
	@Attribute(name="alphaname", required=false)
	private String alphaname;

	@ElementList(name="recipients", inline=false, required=false)
	private ArrayList<Recipient> recipients;

	public Bulk(){
	}

	private final static String TYPE="sms";
	private final static String NAME_DEFAULT=null;
	
	/**
	 * {@link SendMessage} send SMS as package
	 * @param id - client identifier
	 * @param name - client bulk name 
	 * @param alphaname
	 * @param recipients
	 */
	public Bulk(String id,  String name, String alphaname, List<Recipient> recipients){
		this.id=id;
		this.type=TYPE;
		this.name=name;
		this.alphaname=alphaname;
		this.setRecipients(recipients);
	}

	/**
	 * {@link SendMessage} send SMS as package
	 * @param id - client identifier
	 * @param alphaname
	 * @param recipients
	 */
	public Bulk(String id,  String alphaname, List<Recipient> recipients){
		this(id, NAME_DEFAULT, alphaname, recipients);
	}
	
	/**
	 * {@link SendMessage} send SMS as package
	 * @param id - client identifier
	 * @param recipients
	 */
	public Bulk(String id,  List<Recipient> recipients){
		this(id, NAME_DEFAULT, Constants.ALPHA_NAME, recipients);
	}

	/**
	 * {@link SendMessage} send SMS as package
	 * @param id - client identifier
	 * @param recipients
	 */
	public Bulk(List<Recipient> recipients){
		this(null, NAME_DEFAULT, Constants.ALPHA_NAME, recipients);
	}

	/**
	 * {@link QueryMessage} create request for check status of message  
	 * @param bulkId
	 */
	public Bulk(Long bulkId){
		this.bulkId=bulkId;
	}
	
	public Long getBulkId() {
		return bulkId;
	}

	public void setBulkId(Long value) {
		this.bulkId = value;
	}


	public List<Recipient> getRecipients() {
		return recipients;
	}

	public void setRecipients(List<Recipient> recipients) {
		if(recipients==null){
			this.recipients=null;
			return;
		}
		if(recipients instanceof ArrayList){
			this.recipients=(ArrayList<Recipient>) recipients;
		}else{
			this.recipients = new ArrayList<Recipient>(recipients);
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlphaname() {
		return alphaname;
	}

	public void setAlphaname(String alphaname) {
		this.alphaname = alphaname;
	}

	
}
