package engine.database;

/** объект-структура, который содержит список необходимых полей  */
public class ParamEngineSettings {

	/** id профиля  */
	private int idProfile;
	/** название профиля */
	private String name;
	/** описание */
	private String description;
	/** состояние профиля */
	private String profileState;
	/** серийный номер */
	private String serialNumber;
	/** имя устройства  */
	private String nameDevice;
	/** оператор */
	private String operator;
	/** максимальное кол-во повторений */
	private int maxRepeatCount;
	/** задержка перед очередной отправкой */
	private int delayForSendMs;
	/** время ожидания доставки */
	private int delayForGetDelivery;
	/** флаг активности */
	private boolean active;
	/** задержка перед очередным вызовом функции чтения из базы данных информации о новых сообщениях - пауза перед чтением сообщений "на отправку" */
	private int delayForGetMessageForSend;
	/** задержка перед вызовом процедуры отсеивания записей, которые не дождались подтверждения о доставке */
	private int delayForExecuteRepeatController;
	
	
	/** id профиля  */
	public int getIdProfile() {
		return idProfile;
	}

	/** id профиля  */
	public void setIdProfile(int id) {
		this.idProfile = id;
	}

	/** название профиля */
	public String getName() {
		return name;
	}

	/** название профиля */
	public void setName(String name) {
		this.name = name;
	}

	/** описание */
	public String getDescription() {
		return description;
	}


	/** описание */
	public void setDescription(String description) {
		this.description = description;
	}

	/** серийный номер */
	public String getSerialNumber() {
		return serialNumber;
	}

	/** серийный номер */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	/** имя устройства  */
	public String getNameDevice() {
		return nameDevice;
	}

	/** имя устройства  */
	public void setNameDevice(String nameDevice) {
		this.nameDevice = nameDevice;
	}

	/** оператор */
	public String getOperator() {
		return operator;
	}

	/** оператор */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/** максимальное кол-во повторений */
	public int getMaxRepeatCount() {
		return maxRepeatCount;
	}

	/** максимальное кол-во повторений */
	public void setMaxRepeatCount(int maxRepeatCount) {
		this.maxRepeatCount = maxRepeatCount;
	}


	/** задержка перед очередной отправкой */
	public int getDelayForSendMs() {
		return delayForSendMs;
	}


	/** задержка перед очередной отправкой */
	public void setDelayForSendMs(int delayForSendMs) {
		this.delayForSendMs = delayForSendMs;
	}


	/** время ожидания доставки */
	public int getDelayForGetDelivery() {
		return delayForGetDelivery;
	}


	/** время ожидания доставки */
	public void setDelayForGetDelivery(int delayForGetDelivery) {
		this.delayForGetDelivery = delayForGetDelivery;
	}

	/** активность  */
	public boolean getActive() {
		return active;
	}

	/** активность  */
	public void setActive(boolean active) {
		this.active = active;
	}

	/** задержка перед очередным вызовом функции чтения из базы данных информации о новых сообщениях - пауза перед чтением сообщений "на отправку" */
	public int getDelayForGetMessageForSend() {
		return delayForGetMessageForSend;
	}

	/** задержка перед очередным вызовом функции чтения из базы данных информации о новых сообщениях - пауза перед чтением сообщений "на отправку" */
	public void setDelayForGetMessageForSend(int delayForGetMessageForSend) {
		this.delayForGetMessageForSend = delayForGetMessageForSend;
	}

	/** задержка перед вызовом процедуры отсеивания записей, которые не дождались подтверждения о доставке */
	public int getDelayForExecuteRepeatController() {
		return delayForExecuteRepeatController;
	}

	/** задержка перед вызовом процедуры отсеивания записей, которые не дождались подтверждения о доставке */
	public void setDelayForExecuteRepeatController(int delayForExecuteRepeatController) {
		this.delayForExecuteRepeatController = delayForExecuteRepeatController;
	}

	/** получить состояние профиля */
	public String getProfileState() {
		return profileState;
	}

	/** установить состояние профиля */
	public void setProfileState(String profileState) {
		if(profileState!=null){
			this.profileState = profileState.trim();
			if(this.profileState.equalsIgnoreCase("ACTIVE")){
				this.active=true;
			}else{
				this.active=false;
			}
		}else{
			this.profileState=null;
			this.active=false;
		}
	}
}
