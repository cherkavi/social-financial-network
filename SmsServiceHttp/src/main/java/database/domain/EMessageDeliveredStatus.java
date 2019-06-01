package database.domain;

/** 
 * <table> 
 * 	<tr> <td>NONE</td>  <td>доставка не ожидается (<small> устанавливается программой</small>)</td> </tr>
 * 	<tr> <td>DELIVERED</td>  <td>доставлено </td> </tr>
 * 	<tr> <td>ABORTED</td>  <td>отменено </td> </tr>
 * 	<tr> <td>UNKNOWN</td>  <td>неизвестный</td> </tr>
 * </table>
 * */
public enum EMessageDeliveredStatus {
	/** доставка не ожидается */
	NONE(10),
	/** доставлено */
	DELIVERED(11), 
	/** не доставлено */
	ABORTED(12), 
	/** неизвестная ошибка */
	UNKNOWN(13);

	private int kod;
	
	private EMessageDeliveredStatus(int value){
		this.kod=value;
	}
	
	/** код статуса для сохранения  */
	public int getKod(){
		return this.kod;
	}
}
