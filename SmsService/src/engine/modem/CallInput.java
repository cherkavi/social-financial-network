package engine.modem;

/** объект-обертка для входящего сообщения */
public class CallInput {
	private String recipient;
	public CallInput(String recipient){
		this.recipient=recipient;
	}
	
	/** получить номер абонента */
	public String getRecipient(){
		return recipient;
	}
}
