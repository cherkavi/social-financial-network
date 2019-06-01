package com.cherkashyn.vitalii.components.sms.remoteservice.domain;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

@Root(name="recipient")
public class Recipient {
	
	public static enum StatusResponse{
		TOSEND("сообщение находится в очереди на отправку"),
	    ENROUTE("сообщение отправлено, но еще не доставлено адресату"),
	    PAUSED("отправка сообщения приостановлена"),
	    CANCELED("отправка сообщения отменена"),
	    DELIVERED("сообщение доставлено адресату"),
	    FAILED("ошибка отправки сообщения"),
	    EXPIRED("сообщение не доставлено адресату - истек срок доставки"),
	    UNDELIVERABLE("сообщение не может быть доставлено адресату"),
	    REJECTED("сообщение отклонено сервером"),
	    BADCOST("сообщение не доставлено адресату - не определена стоимость сообщения"),
	    UNKNOWN("состояние сообщения не определено");
	     
	    private String description;
	    StatusResponse(String description){
			this.description=description;
		}
	    
		public String getDescription() {
			return description;
		}
	}
	
	public Recipient(){
	}
	
	
	public Recipient(long id, String address, String text){
		this.id=id;
		this.address=address;
		this.text=text;
	}
	
	/** client identifier */
	@Attribute(required=false)
	private long id;
	
	/** SMS current status */
	@Attribute(required=false)
	StatusResponse status;

	/** {@link SendMessage} server identifier, when server sent response */
	@Attribute(required=false)
	private String smsid;

	/** {@link SendMessage} recipient address +380954671434 */
	@Attribute(required=false)
	private String address;
	
	/** recipient message text */
	@Text(required=false)
	private String text;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSmsid() {
		return smsid;
	}

	public void setSmsid(String smsid) {
		this.smsid = smsid;
	}

	public StatusResponse getStatus() {
		return status;
	}

	public void setStatus(StatusResponse status) {
		this.status = status;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "Recipient [id=" + id + ", status=" + status + ", smsid="
				+ smsid + ", address=" + address + ", text=" + text + "]";
	}

}
