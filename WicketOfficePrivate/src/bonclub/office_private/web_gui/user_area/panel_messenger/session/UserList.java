package bonclub.office_private.web_gui.user_area.panel_messenger.session;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import bonclub.office_private.database.wrap.Users;
/** список пользователей */
public class UserList implements Serializable{
	private static final long serialVersionUID = 1L;

	private HashSet<Users> field_user_set=new HashSet<Users>();
	
	/** имя группы для списка пользователей*/
	private String field_list_name;
	
	public UserList(String list_name){
		this.field_list_name=list_name;
	}
	
	/** очистить список пользователей */
	public void clearUsers(){
		this.field_user_set.clear();
	}
	
	/** добавить пользователя в группу*/
	public void addUser(Users user){
		this.field_user_set.add(user);
	}
	
	/** проверить пользователя на нахождение в списке */
	public boolean isUserIntoList(Users user){
		return this.field_user_set.contains(user);
	}

	/** удалить пользователя из списка*/
	public void removeUser(Users user){
		this.field_user_set.remove(user);
	}
	
	/** получить полное имя для данной группы*/
	public String getListName(){
		return this.field_list_name;
	}
	
	/** получить кол-во записей в объекте */
	public int getSize(){
		return this.field_user_set.size();
	}
	
	/** получить список всех пользователей, которые будут разделены через указанный символ
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
	
	/** получить данный объект в виде List*/
	public List<Users> getList(){
		return new ArrayList<Users>(this.field_user_set);
	}
	
	/** установить значение User.selected для всех объектов User*/
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












