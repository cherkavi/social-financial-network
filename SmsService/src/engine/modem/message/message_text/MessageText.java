package engine.modem.message.message_text;

import java.util.Date;

import engine.modem.message.Message;

/** текстовые сообщения для/из модема */
public abstract class MessageText extends Message{
	/** текстовое сообщение для модема  */
	private String text;
	/** уникальный номер, полученный из модема (на отправку, либо получение ) */
	private int refNo;
	
	/** Дата, по которой произошла смена состояния (взято из очереди, отправлено на модем, получен ответ от модема ... ) */
	private Date dateEvent;
	
	/** текстовое сообщение для модема  */
	public String getText() {
		return text;
	}
	
	/** текстовое сообщение для модема  */
	public void setText(String text) {
		this.text = text;
	}

	/** уникальный номер, полученный из модема (на отправку, либо получение ) */
	public int getRefNo() {
		return refNo;
	}

	/** уникальный номер, полученный из модема (на отправку, либо получение ) */
	public void setRefNo(int refNo) {
		this.refNo = refNo;
	}

	/** Дата последнего события, которое произошло с данным элементом  */
	public Date getDateEvent() {
		return dateEvent;
	}

	/** установить дату последнего события по данному элементу */
	public void setDateEvent(Date dateEvent) {
		this.dateEvent = dateEvent;
	}
	

	
}
