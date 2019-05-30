package bc.applet.CardManager.manager;
/** Класс, который содержит уникальные ключи для идентификации клиентов */

public class ClientIdentifier {
	/** уникальное имя клиента, возможно сессионный номер */
	private String field_unique_name;
	
	/** 
	 * Объект для уникальной идентификации клиента
	 * @param unique_name - уникальное имя клиента (возможно, сессионный номер) 
	 */
	public ClientIdentifier(String unique_name){
		this.field_unique_name=unique_name;
	}
	
	/**
	 * Получить уникальный номер
	 */
	public String getUniqueName(){
		return this.field_unique_name;
	}
	
	/** 
	 * Установить уникальный номер 
	 */
	public void setUniqueName(String value){
		this.field_unique_name=value;
	}
}
