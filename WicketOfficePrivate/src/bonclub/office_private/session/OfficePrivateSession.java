package bonclub.office_private.session;

import java.util.HashMap;

import org.apache.wicket.Request;
import org.apache.wicket.protocol.http.WebSession;


public class OfficePrivateSession extends WebSession{
	private static final long serialVersionUID = 1L;
	/** ������, � ������� �������� ������ �������������, ������� ����� ���� ���������� � �������� */
	private HashMap<String,UserList> field_list_of_user=new HashMap<String,UserList>();
	
	/** ������, � ������� �������� String �� ���������� ������ */
	private HashMap<String,String> field_list_of_string=new HashMap<String,String>();
	
	
	/** ���������� ������������� ������������ */
	private Integer customerId;
	/** ����� �� ���������� �������� */
	private PageExchange pageExchange;
	
	/** ����� ���������� ����������*/
	@SuppressWarnings("unused")
	private void Debug(Object information){
		System.out.print("Session ");
		System.out.print("DEBUG");
		System.out.println(information);
	}

	/** ����� ��������� ���������� */
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

	/** ������������� ���� ����� ������� � Default �������� */
	private void initFields(){
		customerId=null;
	}
	
	/** ���������� ���������� ������������� ������������ */
	public Integer getCustomerId(){
		return customerId;
	}
	
	/** �������� �������� ������� ��� ������ ����� ���������� */
	public PageExchange getPageExchange() {
		return pageExchange;
	}

	/** ���������� �������� ������� ��� ������ ����� ���������� */
	public void setPageExchange(PageExchange pageExchange) {
		this.pageExchange = pageExchange;
	}

	/** �������� ������ ������ ����� ���������� */
	public void clearPageExchange(){
		this.pageExchange.clearPageValue();
	}

	/** ���������� ClientId 
	 * @param clientId - ����� ������� 
	 * */
	public Integer setCustomerId(Integer customerId){
		this.customerId=customerId;
		return this.customerId;
	}
	
	/** �������� ���������� ������������� �������� 
	 * ( ��� ���������� ������, ������� ����������� ������������  )
	 * */
	public void clearCustomerId(){
		this.customerId=null;
	}
	
	/** �������� ����� ������ � �������� ������ � ������ 
	 * @param key ����, �� �������� ����� �������� ������ ������
	 * @param value ��������
	 * */
	public void putString(String key, String value){
		this.field_list_of_string.put(key, value);
	}
	/** ������� ������ �� ��������� ����� �� ������ 
	 * @param key - ����, �� �������� ����� ������� ������ 
	 * */
	public void removeString(String key){
		this.field_list_of_string.remove(key);
	}
	/** �������� ������ � �������� ������ �� ������ 
	 * @param key - ����, �� �������� ����� �������� ������ 
	 * */
	public String getString(String key){
		return this.field_list_of_string.get(key);
	}
	
	/** �������� � ������ ������ �������������, ���� ������ ������ ��������� (�� �����) �� ��� ����� ������������
	 * @param name ��� ����������������� ������ 
	 * @param list ������ �������������
	 */
	public void addUserList(UserList list){
		if(list!=null){
			this.field_list_of_user.remove(list.getListName());
			this.field_list_of_user.put(list.getListName(), list);
		}
	}
	
	/** �������� �� ������ ������ ������������� �� ����������� ����� 
	 * @param list_name - ��� ������, ������� ������ ���� � ����� ������
	 * @return UserList ��� null, ���� �������� ��� �� ����������   
	 * */
	public UserList getUserList(String list_name){
		return this.field_list_of_user.get(list_name);
	}
	
	/** 
	 * ������� ������ �� ������� �������� 
	 * @param name ��� ������, ������� ������ ���� ������ 
	 */
	public void removeList(String name){
		this.field_list_of_user.remove(name);
	}
	
}
