package engine.modem.message.message_text;

import java.util.Date;

import engine.modem.message.EMessageActionSendState;

/** исходящее сообщение - сообщение для отправки  */
public class MessageOut extends MessageText{
	/** статус сообщения */
	private int idMessageStatus;
	/**  код статуса отправки  */
	private int idSendStatus;
	/** кол-во повторов, при неприходе сообщения о подтверждении  */
	private int repeatCount;
	/** является ли сообщение архивным  */
	private int archiv;
	/** причина помещения данного сообщения в архив  */
	private String archivReason;
	private Date actionDate;
	private EMessageActionSendState state;
	private int profileId;
	/** уникальный идентификатор MessageAction из базы данных */
	private int actionId;
	
	public int getIdMessageStatus() {
		return idMessageStatus;
	}
	public void setIdMessageStatus(int idMessageStatus) {
		this.idMessageStatus = idMessageStatus;
	}
	public int getIdSendStatus() {
		return idSendStatus;
	}
	public void setIdSendStatus(int idSendStatus) {
		this.idSendStatus = idSendStatus;
	}
	public int getRepeatCount() {
		return repeatCount;
	}
	public void setRepeatCount(int repeatCount) {
		this.repeatCount = repeatCount;
	}
	public int getArchiv() {
		return archiv;
	}
	public void setArchiv(int archiv) {
		this.archiv = archiv;
	}
	public String getArchivReason() {
		return archivReason;
	}
	public void setArchivReason(String archivReason) {
		this.archivReason = archivReason;
	}
	
	/** установить дату активности, дату, с которой стоит учитывать данное сообщение как "видимое", до этого момента - считать сообщение "невидимым"*/
	public void setActionDate(Date date) {
		this.actionDate=date;
	}
	
	/** получить дату активности, дату, с которой стоит учитывать данное сообщение как "видимое", до этого момента - считать сообщение "невидимым"*/
	public Date getActionDate(){
		return this.actionDate;
	}
	
	/** состояние сообщения 
	 * @param состояние данного сообщения {@link EMessageActionSendState} 
	 */
	public void setState(EMessageActionSendState state) {
		this.state=state;
	}

	/** получить состояние данного сообщения  
	 * @return {@link EMessageActionSendState} 
	 */
	public EMessageActionSendState getState(){
		return this.state;
	}
	
	/** установить уникальный номер профиля */
	public void setProfileId(int value) {
		this.profileId=value;
	}
	
	/** получить уникальный номер профиля  */ 
	public int getProfileId(){
		return this.profileId;
	}
	
	/** установить уникальный идентификатор MessageAction по данному сообщению 
	 * <br>
	 * <small>имеется две таблицы в базе данных - MESSAGE(сообщения) и MESSAGE_ACTION(события/состояния по этим сообщениям) </small>
	 *  
	 */
	public void setActionId(int actionId) {
		this.actionId=actionId;
	}
	
	/** получить уникальный идентификатор MessageAction для данного сообщения (код из таблицы MESSAGE_ACTION, в которой хранятся все события 
	 * по данному сообщению) 
	 */
	public int getActionId(){
		return this.actionId;
	}
}
