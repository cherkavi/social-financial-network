package bonclub.office_private.web_service.db_function.wrap;

/** базовый класс для всех оберток над ответами процедур и функций */
public class Wrap {
	
	/** проверить, возвращает ли функция строковое значение "0" - что сигнализирует (по договоренности) о положительной обработке параметра 
	 * @param value - значение, которое вернула функция 
	 * @return
	 * <li><b> true</b>  функция отработала без ошибок </li>
	 * <li><b>false</b> есть ошибки при отработке </li>
	 */
	public boolean checkReturnValueForZero(String value){
		if(value!=null){
			return value.trim().equalsIgnoreCase("0");
		}else{
			return false;
		}
	}

	/** проверить, возвращает ли функция строковое значение "0" - что сигнализирует (по договоренности) о положительной обработке параметра 
	 * <li><b> true</b>  функция отработала без ошибок </li>
	 * <li><b>false</b> есть ошибки при отработке </li>
	 * */
	public boolean checkReturnValueForZero(){
		if(this.getReturnValue()!=null){
			return this.getReturnValue().trim().equalsIgnoreCase("0");
		}else{
			return false;
		}
	}
	
	
	/** результирующее сообщение, обычно то(по-договоренности), в которое возвращается ошибка в виде текста */
	protected String resultMessage;
	/** возвращаемое функцией значение, которое нужно проверять на "0" функцией {@link #checkReturnValueForZero} (по-договоренности) */
	protected String returnValue;

	/**
	 * @return результирующее сообщение, обычно то(по-договоренности), в которое возвращается ошибка в виде текста 
	 */
	public String getResultMessage() {
		return resultMessage;
	}
	/**
	 * @param resultMessage результирующее сообщение, обычно то(по-договоренности), в которое возвращается ошибка в виде текста 
	 */
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	/**
	 * @return the returnValue возвращаемое функцией значение, которое нужно проверять на "0" функцией {@link #checkReturnValueForZero} (по-договоренности)
	 */
	public String getReturnValue() {
		return returnValue;
	}
	/**
	 * @param returnValue возвращаемое функцией значение, которое нужно проверять на "0" функцией {@link #checkReturnValueForZero} (по-договоренности)
	 */
	public void setReturnValue(String returnValue) {
		this.returnValue = returnValue;
	}
	
	
}
