package database.domain;

/** 
 * <table>
 * 	<tr> <td>IN_PROCESS</td> <td> в процессе </td> </tr>
 * 	<tr> <td>WAIT_FOR_CONFIRM</td> <td>ожидание подтверждения о доставке</td> </tr>
 * 	<tr> <td>SEND_ERROR</td> <td> ошибка отправки</td> </tr>
 * </table>
 * */
public enum EMessageActionSendState {
	/** в процессе  */
	IN_PROCESS("в процессе", 11),
	/** послано модемом - ожидается подтверждение доставки  от модема */
	WAIT_FOR_CONFIRM("ожидание подтверждения о доставке", 12),
	/** ошибка доставки */
	SEND_ERROR("ошибка отправки", 13);
	
	/** описание акронима */
	private String description;
	
	/** код в базе данных */
	private int kod;
	
	/** 
	 * @param description - описание 
	 */
	private EMessageActionSendState(String description, int kod) {
		this.kod=kod;
		this.description=description;
	}

	/** получить описание состояния  */
	public String getDescription(){
		return this.description;
	}
	
	public int getKod(){
		return this.kod;
	}
}
