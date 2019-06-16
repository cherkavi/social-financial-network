package bonclub.office_private.web_gui.user_area.panel_messenger.session;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import bonclub.office_private.database.wrap.Users;
/** ������ ������������� */
public class UserList implements Serializable{
	private static final long serialVersionUID = 1L;

	private HashSet<Users> field_user_set=new HashSet<Users>();
	
	/** ��� ������ ��� ������ �������������*/
	private String field_list_name;
	
	public UserList(String list_name){
		this.field_list_name=list_name;
	}
	
	/** �������� ������ ������������� */
	public void clearUsers(){
		this.field_user_set.clear();
	}
	
	/** �������� ������������ � ������*/
	public void addUser(Users user){
		this.field_user_set.add(user);
	}
	
	/** ��������� ������������ �� ���������� � ������ */
	public boolean isUserIntoList(Users user){
		return this.field_user_set.contains(user);
	}

	/** ������� ������������ �� ������*/
	public void removeUser(Users user){
		this.field_user_set.remove(user);
	}
	
	/** �������� ������ ��� ��� ������ ������*/
	public String getListName(){
		return this.field_list_name;
	}
	
	/** �������� ���-�� ������� � ������� */
	public int getSize(){
		return this.field_user_set.size();
	}
	
	/** �������� ������ ���� �������������, ������� ����� ��������� ����� ��������� ������
	 * @param delimeter
	 * */
	public String getListDelimeterString(String delimeter){
		StringBuffer return_value=new StringBuffer();
		delimeter=", ";
		Iterator<Users> iterator=this.field_user_set.iterator();
		Users current_user;
		while(iterator.hasNext()){
			current_user=iterator.next();
			if(return_value.length()>0){
				return_value.append(delimeter);
			}
			return_value.append(current_user.getNick());
		}
		return return_value.toString();
	}
	
	/** �������� ������ ������ � ���� List*/
	public List<Users> getList(){
		return new ArrayList<Users>(this.field_user_set);
	}
	
	/** ���������� �������� User.selected ��� ���� �������� User*/
	public UserList setSelected(boolean value){
		Iterator<Users> iterator=this.field_user_set.iterator();
		Users current_user;
		while(iterator.hasNext()){
			current_user=iterator.next();
			current_user.setSelected(value);
		}
		return this;
	}
}












