package bonclub.office_private.session;

import java.util.HashMap;

import org.apache.wicket.Request;
import org.apache.wicket.protocol.http.WebSession;


public class OfficePrivateSession extends WebSession{
	private static final long serialVersionUID = 1L;
	/** объект, в котором хранятся списки пользователей, которые могут быть необходимы в процессе */
	private HashMap<String,UserList> field_list_of_user=new HashMap<String,UserList>();
	
	/** объект, в котором хранятся String по уникальным именам */
	private HashMap<String,String> field_list_of_string=new HashMap<String,String>();
	
	
	/** уникальный идентификатор пользователя */
	private Integer customerId;
	/** ответ от предыдущей страницы */
	private PageExchange pageExchange;
	
	/** вывод отладочной информации*/
	@SuppressWarnings("unused")
	private void Debug(Object information){
		System.out.print("Session ");
		System.out.print("DEBUG");
		System.out.println(information);
	}

	/** вывод ошибочной информации */
	@SuppressWarnings("unused")
	private void Error(Object information){
		System.err.println("Session ");
		System.err.println("ERROR ");
		System.err.println(information);
	}
	
	
	public OfficePrivateSession(Request request) {
		super(request);
		initFields();
	}

	/** инициализация всех полей объекта в Default значения */
	private void initFields(){
		customerId=null;
	}
	
	/** возвратить уникальный идентификатор пользователя */
	public Integer getCustomerId(){
		return customerId;
	}
	
	/** получить значение объекта для обмена между страницами */
	public PageExchange getPageExchange() {
		return pageExchange;
	}

	/** установить значение объекта для обмена между страницами */
	public void setPageExchange(PageExchange pageExchange) {
		this.pageExchange = pageExchange;
	}

	/** очистить объект обмена между страницами */
	public void clearPageExchange(){
		this.pageExchange.clearPageValue();
	}

	/** установить ClientId 
	 * @param clientId - номер клиента 
	 * */
	public Integer setCustomerId(Integer customerId){
		this.customerId=customerId;
		return this.customerId;
	}
	
	/** очистить уникальный идентификатор партнера 
	 * ( для завершения сессии, которую инициировал пользователь  )
	 * */
	public void clearCustomerId(){
		this.customerId=null;
	}
	
	/** добавить новую строку с заданным именем в сессию 
	 * @param key ключ, по которому нужно положить данную строку
	 * @param value значение
	 * */
	public void putString(String key, String value){
		this.field_list_of_string.put(key, value);
	}
	/** удалить строку по заданному имени из сессии 
	 * @param key - ключ, по которому нужно удалить строку 
	 * */
	public void removeString(String key){
		this.field_list_of_string.remove(key);
	}
	/** получить строку с заданным именем из сессии 
	 * @param key - ключ, по которому нужно получить строку 
	 * */
	public String getString(String key){
		return this.field_list_of_string.get(key);
	}
	
	/** добавить в сессию список пользователей, если данная группа сущесвует (по имени) то она будет перезаписана
	 * @param name имя пользовательского списка 
	 * @param list список пользователей
	 */
	public void addUserList(UserList list){
		if(list!=null){
			this.field_list_of_user.remove(list.getListName());
			this.field_list_of_user.put(list.getListName(), list);
		}
	}
	
	/** получить из сессии список пользователей по уникальному имени 
	 * @param list_name - имя списка, который должен быть в нашей сессии
	 * @return UserList или null, если заданное имя не существует   
	 * */
	public UserList getUserList(String list_name){
		return this.field_list_of_user.get(list_name);
	}
	
	/** 
	 * удалить список из объекта списоков 
	 * @param name имя списка, который должен быть удален 
	 */
	public void removeList(String name){
		this.field_list_of_user.remove(name);
	}
	
}
