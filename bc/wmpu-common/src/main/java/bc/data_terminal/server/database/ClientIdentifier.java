package bc.data_terminal.server.database;

import java.sql.Connection;

/** �����, ������� �������� ������ ��� ����������� ������������� �������<br>
 *  <strong> ������������ ��� ��������� ID_TERM - ������������������ ������ ��������� ��� ������� � ����� ������</strong><br>
 *  <i>���� ������ ������ ���� ������� � ������� ������� � ����� - ����� DBFunction ������ � ���������������
 *  ����������� ( ���� ������� ������������ ) ��� ��������� ����������� ������ �� ���������� �������</i> 
 * */
public class ClientIdentifier {
	/** Terminal LOGIN */
	private String field_login="";
	/** Terminal PASSWORD */
	private String field_password="";
	/** ���������� ������������� ���������, ������� ����� ��������, ������ �� Login � Password*/
	private String field_terminal_id="";
	
	/** ���������� ������������� ������� � ���� ������, �� ��������� ������ � ������, ������� ���� ������������ */
	public ClientIdentifier(String login, String password,Connection connection){
		this.field_login=login;
		this.field_password=password;
		DBFunction function=new DBFunction();
		function.fillIdentifier(this,connection);
	}

	/** ���������� ������������� ������� � ���� ������, �� ��������� ������ � ������, ������� ���� ������������ */
	public ClientIdentifier(String login, String password,DBFunction function, Connection connection){
		this.field_login=login;
		this.field_password=password;
		function.fillIdentifier(this,connection);
	}
	
	/** ���������� ����� */
	public void setLogin(String login){
		this.field_login=login;
	}
	
	/** ���������� ������ */
	public void setPassword(String password){
		this.field_password=password;
	}
	
	/** �������� ����� */
	public String getLogin(){
		return this.field_login;
	}
	/** �������� ������ */
	public String getPassword(){
		return this.field_password;
	}
	
	/** ���������� ��� ��� ��������� */
	public void setTerminalId(String terminal_id){
		this.field_terminal_id=terminal_id;
	}
	
	/** �������� ��� ��������� */
	public String getTerminalId(){
		return this.field_terminal_id;
	}
	
	/** ���������� true, ���� ���������� ������������� ��������� ��������� (�� ���������� � ������ �������)*/
	public boolean isTerminalIdIdentify(){
		return this.field_terminal_id.length()>0;
	}
}
