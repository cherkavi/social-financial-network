package database;

public class ServiceSettings {
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
	public int getIdProfile() {
		return idProfile;
	}
	public void setIdProfile(int idProfile) {
		this.idProfile = idProfile;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getProfileState() {
		return profileState;
	}
	public void setProfileState(String profileState) {
		this.profileState = profileState;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getNameDevice() {
		return nameDevice;
	}
	public void setNameDevice(String nameDevice) {
		this.nameDevice = nameDevice;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public int getMaxRepeatCount() {
		return maxRepeatCount;
	}
	public void setMaxRepeatCount(int maxRepeatCount) {
		this.maxRepeatCount = maxRepeatCount;
	}
	public int getDelayForSendMs() {
		return delayForSendMs;
	}
	public void setDelayForSendMs(int delayForSendMs) {
		this.delayForSendMs = delayForSendMs;
	}
	public int getDelayForGetDelivery() {
		return delayForGetDelivery;
	}
	public void setDelayForGetDelivery(int delayForGetDelivery) {
		this.delayForGetDelivery = delayForGetDelivery;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}

	
}
