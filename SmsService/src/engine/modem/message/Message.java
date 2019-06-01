package engine.modem.message;

import java.util.Date;

import engine.modem.message.call.Call;
import engine.modem.message.call.CallIn;
import engine.modem.message.call.CallOut;
import engine.modem.message.message_text.MessageIn;
import engine.modem.message.message_text.MessageInReport;
import engine.modem.message.message_text.MessageInText;
import engine.modem.message.message_text.MessageOut;
import engine.modem.message.message_text.MessageText;

/** события для устройства */
public abstract class Message {
	private int id;
	private String recipient;
	private Date createDate;
	private int idProfile;

	/** уникальный идентификатор в базе данных  */
	public int getId() {
		return id;
	}
	
	/** уникальный идентификатор в базе данных  */
	public void setId(int id) {
		this.id = id;
	}
	
	/** телефонный номер  */
	public String getRecipient() {
		return recipient;
	}

	/** телефонный номер  */
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	/** дата создания  */
	public Date getCreateDate() {
		return createDate;
	}
	
	/** дата создания  */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	/** уникальный номер профиля  */
	public int getIdProfile() {
		return idProfile;
	}
	
	/** уникальный номер профиля  */
	public void setIdProfile(int idProfile) {
		this.idProfile = idProfile;
	}
	
	/** получить тип сообщения 
	 * <table>
	 * 	<tr></tr>
	 * </table>
	 *  
	 *  */
	public static String getType(Message object){
		if(object!=null){
			if(object instanceof Call){
				if(object instanceof CallIn){
					return "CALL_IN";
				}else if(object instanceof CallOut){
					return "CALL_OUT";
				}else{
					return "CALL";
				}
			}else if(object instanceof MessageText){
				if(object instanceof MessageIn){
					if(object instanceof MessageInReport){
						return "MESSAGE_IN_REPORT";
					}else if(object instanceof MessageInText){
						return "MESSAGE_IN_TEXT";
					}else{
						return "MESSAGE_IN";
					}
				}else if(object instanceof MessageOut){
					return "MESSAGE_OUT";
				}else {
					return "MESSAGE";
				}
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
}
