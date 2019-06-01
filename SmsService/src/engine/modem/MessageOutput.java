package engine.modem;

import org.smslib.OutboundMessage;
import org.smslib.Message.MessageEncodings;

/** исходящее сообщение */
public class MessageOutput {
	/** сообщение контекста SMSLib*/
	private OutboundMessage message=null;
	/** уникальный номер в базе данных (MESSAGE.ID)*/
	private Integer idDataBase=null;
	/** уникальный номер идентификатора по данному сообщению в таблице ACTION*/
	private int idAction;
	
	/** 
	 *  
	 * @param recipient абонент, которому должно быть доставлено сообщение
	 * @param text текст сообщения 
	 * @param statusReport нужно ли требовать статус о доставке данного сообщения 
	 */
	public MessageOutput(String recipient, 
					     String text,
					     Integer idDataBase,
					     int idAction){
		message=new OutboundMessage(recipient, text);
		//message.getOutboundMessage().setFrom("BonClub");
		if(isRussianLetter(text)){
			// INFO modem установить кодировку для отправки сообщения
			message.setEncoding(MessageEncodings.ENCUCS2);
		}
		// INFO modem установка обязательного прихода подтверждений отравки сообщений для данного SMS
		message.setStatusReport(true);
		this.idDataBase=idDataBase;
		this.idAction=idAction;
	}

	private boolean isRussianLetter(String text){
		boolean returnValue=false;
		if(text!=null){
			for(int counter=0;counter<text.length();counter++){
				if(text.charAt(counter)>1000){
					returnValue=true;
					break;
				}
			}
		}
		return returnValue;
	}
	
	public static void main(String[] args){
		System.out.println("begin");
		String message=new String("hello привет");
		for(int counter=0;counter<message.length();counter++){
			System.out.println(0+message.charAt(counter));
		}
		System.out.println("end");
	}
	
	/** получить сообщение для отправки в удобочитаемом виде для org.smslib */
	public OutboundMessage getOutboundMessage(){
		return this.message;
	}

	/** уникальный номер в базе данных (MESSAGE.ID)*/
	public Integer getIdDataBase() {
		return idDataBase;
	}
	
	/** уникальный номер в базе данных (MESSAGE.ID)*/
	public void setIdDataBase(Integer idDataBase) {
		this.idDataBase = idDataBase;
	}

	public String getRefNo() {
		return this.message.getRefNo();
	}

	/** уникальный номер записи в таблице Action по данному сообщению */
	public int getActionId(){
		return this.idAction;
	}
}
