package engine;

import java.sql.Connection;

import database.Connector;
import engine.database.DatabaseProxy;
import engine.database.ParamEngineSettings;

/** настройки профиля отправщика ( настройка движка отчетов ) 
 * <статический класс - класс, имеющий >
 * */
public class EngineSettings {
	/** наложение запрета на создание экземляров данного объекта  */
	private EngineSettings(){
	}
	
	/** */
	private static int idProfile;
	/** */
	private static String profileState;
	/** */
	private static String name;
	/** */
	private static String description;
	/** */
	private static int kodState;
	/** */
	private static String serialNumber;
	/** */
	private static String nameDevice;
	/** */
	private static String operator;
	/** */
	private volatile static int maxRepeatCount;
	/** */
	private volatile static int delayForSendMs;
	/** */
	private volatile static int delayForGetDelivery;
	/** */
	private volatile static int delayForGetMessageForSend;
	/** */
	private volatile static int delayForExecuteRepeatController;
	
	/** флаг активности данного профиля  
	 * <ul> <b>true</b> активен </ul>
	 * <ul> <b>false</b> не активен</ul>
	 * */
	private static volatile boolean active;
	
	/** загрузка данных профиля  
	 * @param connection - соединение с базой данных
	 * @param deviceIMEI - уникальный номер устройства, на основании которого будет происходить отправка данных 
	 * @return 
	 * <li> <b>null</b> - чтение прошло успешно  </li> 
	 * <li> <b>not null</b> - ошибка чтения данных </li> 
	 * */
	private static String load(String deviceIMEI){
		// INFO query: настройки профиля, загрузка из базы   
		Connection connection=null;
		try{
			idProfile=0;
			name="";
			description="";
			kodState=0;
			serialNumber="";
			nameDevice="";
			operator="";
			
			connection=Connector.getConnection();
			DatabaseProxy proxy=new DatabaseProxy();
			ParamEngineSettings newSettings=null;
			
			newSettings=proxy.loadEngineSettings(connection,deviceIMEI);
			if(newSettings!=null){
				idProfile=newSettings.getIdProfile();
				profileState=newSettings.getProfileState();
				name=newSettings.getName();
				description=newSettings.getDescription();
				serialNumber=IMEI;
				active=newSettings.getActive();
				nameDevice=newSettings.getNameDevice();
				operator=newSettings.getOperator();
				maxRepeatCount=0;
				maxRepeatCount=newSettings.getMaxRepeatCount();
				
				delayForSendMs=0;
				delayForSendMs=newSettings.getDelayForSendMs();
				
				delayForGetDelivery=0;
				delayForGetDelivery=newSettings.getDelayForGetDelivery();
				delayForGetMessageForSend=newSettings.getDelayForGetMessageForSend();
				delayForExecuteRepeatController=newSettings.getDelayForExecuteRepeatController();
				return null;
			}else{
				String message="EngineSettings does not loaded";
				System.err.println(message);
				return message;
			}
		}catch(Exception ex){
			String returnValue="EngineSettings#load "+ex.getMessage();
			System.err.println(returnValue);
			return returnValue;
		}finally{
			try{
				connection.close();
			}catch(Exception ex){};
		}
	}

	/** уникальный код устройства, которое будет передавать данные  */
	private static String IMEI;
	
	
	/** Прочесть параметры инициализации для профиля из базы и запустить поток, который будет обновлять данные через указанный интервал времени 
	 * @param IMEI уникальный код устройства, которое будет передавать данные ( IMEI модема )
	 * @param waitSettingsUpdateMs время в милисекундах перед очередным обновлением данных настроек ( на основании чтения из базы данных ) 
	 * */
	public static void loadAndInitReader(String IMEI, 
										 final int waitSettingsUpdateMs){
		EngineSettings.IMEI=IMEI;
		// загрузка профиля данных
		EngineSettings.load(EngineSettings.IMEI);
		Thread thread=new Thread(new Runnable(){
			public void run(){
				while(true){
					try{
						Thread.sleep(waitSettingsUpdateMs);
					}catch(InterruptedException ie){};
					// очередная загрузка профиля данных 
					EngineSettings.load(EngineSettings.IMEI);
				}
			}
		});
		thread.setDaemon(true); // можно не делать Daemon, т.к. данный поток по определению будет являтся главным
		thread.start();
	}
	
	/** получить уникальный номер профиля  
	 * <br>
	 * Все возможные состояния профиля могут быть получены:
	 * <b><small> select * from bc_admin.vc_ds_profile_state_all </small></b> 
	 * */
	public static int getProfileId() {
		return idProfile;
	}

	/** получить состояние профиля  */
	public static String getProfileState(){
		return profileState;
	}
	
	/** получить имя профиля */
	public static String getName() {
		return name;
	}

	/** описание профиля  */
	public static String getDescription() {
		return description;
	}

	/** получить код состояния  */
	public static int getKodState() {
		return kodState;
	}

	/** получить серийный номер устройства */
	public static String getSerialNumber() {
		return serialNumber;
	}

	/** получить имя устройства, которое будет отправлять SMS-сообщения  */
	public static String getNameDevice() {
		return nameDevice;
	}

	/** получить имя оператора для отправки */
	public static String getOperator() {
		return operator;
	}

	/** максимальное кол-во повторов в случае не получения подстверждения о доставке  */
	public static int getMaxRepeatCount() {
		return maxRepeatCount;
	}

	/** ожидание перед очередной отправкой */
	public static int getDelayForSendMs() {
		return delayForSendMs;
	}

	/** кол-во секунд для ожидания подтверждения получения удаленным адресатом SMS -сообщения, при выходе за рамки данного параметра, SMS считается не доставленным */
	public static int getDelayForWaitDelivery() {
		return delayForGetDelivery;
	}

	/** является ли данный профиль активным  */
	public static boolean isActive(){
		return active;
	}

	/** получить задержку перед очередным опросом базы на наличие в ней сообщений на отправку */
	public static int getDelayForGetMessageForSend(){
		return delayForGetMessageForSend;
	}
	
	/** получить задержку перед очередным вызовом функции, которая будет "отсеивать" сообщения, которые не дождались положительного подтверждения о доставке сообщения */
	public static int getDelayForExecuteRepeatController(){
		return delayForExecuteRepeatController;
	}
	
}
