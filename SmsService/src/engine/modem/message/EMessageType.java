package engine.modem.message;

/** тип сообщений 
 * <ul>CALL_IN </ul>
 * <ul>CALL_OUT </ul>
 * <ul>RECEIVE </ul>
 * <ul>SEND </ul>
 * */
public enum EMessageType {
	/** входящий звонок */
	CALL_IN, 
	/** исходящий звонок */
	CALL_OUT, 
	/** принятое сообщение (сообщение)*/
	RECEIVE, 
	/** сообщение для отправки (посланное сообщение )*/
	SEND;
}
