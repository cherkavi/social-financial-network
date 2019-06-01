package gui.Utility;

import xml_ini.Loader;

/** общий объект для всех графических окон, который несет в себе необходимые ссылки на объекты */
public class CommonObject {
	/** соединение с базой данных */
	private Loader loader=null;

	/** общий объект для всех графических окон, который несет в себе необходимые ссылки на объекты 
	 * @param pathToXml - путь к файлу XML
	 * @throws Exception - выбрасывает исключение, если файла XML не найден, либо не смог быть создан
	 * */
	public CommonObject(String pathToXml) throws Exception {
		loader=new Loader(pathToXml);
	}
	
	public String getComPortName(){
		// System.out.println("ComPort:"+this.loader.getValue("//SETTINGS/COM_PORT_NAME"));
		return this.loader.getValue("//SETTINGS/COM_PORT_NAME");
	}
	
	public String getComPortSpeed(){
		// System.out.println("ComPort speed:"+this.loader.getValue("//SETTINGS/COM_PORT_SPEED"));
		return this.loader.getValue("//SETTINGS/COM_PORT_SPEED");
	}
	
	public String getDataBaseUrl(){
		// System.out.println("DataBaseUrl:"+this.loader.getValue("//SETTINGS/DATABASE_URL"));
		return this.loader.getValue("//SETTINGS/DATABASE_URL");
	}
	
	public String getDataBaseUser(){
		// System.out.println("DataBaseUser:"+this.loader.getValue("//SETTINGS/DATABASE_USER"));
		return this.loader.getValue("//SETTINGS/DATABASE_USER");
	}

	public String getDataBasePassword(){
		// System.out.println("DataBasePassword:"+this.loader.getValue("//SETTINGS/DATABASE_PASSWORD"));
		return this.loader.getValue("//SETTINGS/DATABASE_PASSWORD");
	}
	
	/* получить уровень логгирования (DEBUG, INFO_, WARN, ERROR)
	public String getLoggerLevel(){
		System.out.println("Log level:"+this.loader.getValue("//SETTINGS/LOGGER_LEVEL"));
		return this.loader.getValue("//SETTINGS/LOGGER_LEVEL");
	}*/
	
	/* получить имя файла, куда нужно складывать логи  
	public String getLogFile(){
		return this.loader.getValue("//SETTINGS/LOGGER_FILE");
	}*/
	
	/** получить значение, которое "скажет" является ли данное приложение графическим (false) или консольным (true) */
	public String isConosle(){
		String value=this.loader.getValue("//SETTINGS/IS_CONSOLE");
		if(value==null){
			value="";
		};
		// System.out.println("DataBasePassword:"+value);
		return value.trim();
	}
	
	
	public static void main(String[] args){
		Boolean booleanValue=Boolean.parseBoolean("True");
		System.out.println(booleanValue);
	}

	/** получить уникальный номер устройства ( модема ) для его идентификации в базе данных (для получения профиля) */
	public String getModemNumber() {
		return this.loader.getValue("//SETTINGS/MODEM");
	}
	
	/*преобразовать текстовое представление в значение {@link org.apache.log4j.Level Level} 
	public static Level convertStringLevel(String levelValue){
		Level returnValue=Level.DEBUG;
		if(levelValue!=null){
			String value=levelValue.trim().toUpperCase();
			if(value.equalsIgnoreCase("off")){
				returnValue=Level.OFF;
			}
			if(value.equalsIgnoreCase("debug")){
				returnValue=Level.DEBUG;
			}
			if(value.equalsIgnoreCase("info")){
				returnValue=Level.INFo;
			}
			if(value.equalsIgnoreCase("warn")){
				returnValue=Level.WARN;
			}
			if(value.equalsIgnoreCase("error")){
				returnValue=Level.ERROR;
			}
			if(value.equalsIgnoreCase("trace")){
				returnValue=Level.TRACE;
			}
		}
		return returnValue;
	}
*/
	
	/* получить значение шаблона Pattern 
	public String getLogPattern() {
		String value=this.loader.getValue("//SETTINGS/LOGGER_PATTERN");
		if(value==null){
			value="";
		};
		System.out.println("LoggerPattern:"+value);
		return value.trim();
	}
	*/
}
