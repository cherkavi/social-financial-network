package engine.modem;

/** уведомление об исходящих сообщениях */
public interface IMessageOutputListener {
	/** передать сообщение, которое было послано, и передать
	 * @param message - сообщение, которое было послано через шлюз
	 * @param sended = true, если это сообщение было передано устройством,
	 *  ( false, если сообщение передно не было )
	 * */
	public void messageOutput(MessageOutput message, boolean sended);
}
