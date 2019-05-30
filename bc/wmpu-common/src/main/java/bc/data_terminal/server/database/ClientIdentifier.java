package bc.data_terminal.server.database;

import java.sql.Connection;

/** класс, который содержит данные для однозначной идентификации клиента<br>
 *  <strong> предназначен для получения ID_TERM - идентификационного номера терминала при общении с базой данных</strong><br>
 *  <i>этот объект должен быть передан в функции общения с базой - класс DBFunction вместе с дополнительными
 *  параметрами ( если таковые присутствуют ) для получения необходимых данных по указанному клиенту</i> 
 * */
public class ClientIdentifier {
	/** Terminal LOGIN */
	private String field_login="";
	/** Terminal PASSWORD */
	private String field_password="";
	/** уникальный идентификатор терминала, который можно получить, исходя из Login и Password*/
	private String field_terminal_id="";
	
	/** уникальный идентификатор клиента в базе данных, на основании логина и пароля, которые ввел пользователь */
	public ClientIdentifier(String login, String password,Connection connection){
		this.field_login=login;
		this.field_password=password;
		DBFunction function=new DBFunction();
		function.fillIdentifier(this,connection);
	}

	/** уникальный идентификатор клиента в базе данных, на основании логина и пароля, которые ввел пользователь */
	public ClientIdentifier(String login, String password,DBFunction function, Connection connection){
		this.field_login=login;
		this.field_password=password;
		function.fillIdentifier(this,connection);
	}
	
	/** установить логин */
	public void setLogin(String login){
		this.field_login=login;
	}
	
	/** установить пароль */
	public void setPassword(String password){
		this.field_password=password;
	}
	
	/** получить логин */
	public String getLogin(){
		return this.field_login;
	}
	/** получить пароль */
	public String getPassword(){
		return this.field_password;
	}
	
	/** установить код для терминала */
	public void setTerminalId(String terminal_id){
		this.field_terminal_id=terminal_id;
	}
	
	/** получить код терминала */
	public String getTerminalId(){
		return this.field_terminal_id;
	}
	
	/** возвращает true, если уникальный идентификатор терминала определен (не содержится в данном объекте)*/
	public boolean isTerminalIdIdentify(){
		return this.field_terminal_id.length()>0;
	}
}
