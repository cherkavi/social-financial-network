package logger.utility;

/** условие, которое будет проверять логгер */
public interface ILoggerCondition {
	/** условие, которое проверяет логгер
	 * @return 
	 * <ul>
	 * 	<li><b>true</b> - true - необходимо обновить логгер </li>
	 * 	<li><b>false</b> - false - нет нужды обновлять логгер </li>
	 * </ul>
	 *  */
	public boolean condition();
}
