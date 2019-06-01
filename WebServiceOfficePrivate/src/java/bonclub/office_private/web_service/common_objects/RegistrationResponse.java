package bonclub.office_private.web_service.common_objects;

import java.io.Serializable;
import java.util.Date;


/** ����� ����������� ���� ������ �� ��������� ������ {@link RegistrationRequest} ������� OfficePrivate �� ��������� ����� ������ � �������    */
public class RegistrationResponse  implements Serializable{
	private static final long serialVersionUID = 1664010852255176509L;
	
	/** ���������� ������������� ������������ � �������� ��������� ���� */
	private int parentId;
	private String login;
	private String password;
	private Date dateRegistration;
	private String name;
	private String surname;
	private String fathername;
	private String errorMessage;
	
	/** ����� ����������� ���� ������ �� ��������� ������ {@link RegistrationRequest} ������� OfficePrivate �� ��������� ����� ������ � �������    */
	public RegistrationResponse(){
	}

	/**
	 * @return the parentId
	 */
	public int getParentId() {
		return parentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}



	/**
	 * @return �����
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return ������
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return ���� ����������� � ������� 
	 */
	public Date getDateRegistration() {
		return dateRegistration;
	}

	/**
	 * @param dateRegistration the dateRegistration to set
	 */
	public void setDateRegistration(Date dateRegistration) {
		this.dateRegistration = dateRegistration;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}

	/**
	 * @return the fathername
	 */
	public String getFathername() {
		return fathername;
	}

	/**
	 * @param fathername the fathername to set
	 */
	public void setFathername(String fathername) {
		this.fathername = fathername;
	}

	/** �������� ���������� �� ������ 
	 * @return 
	 * <ul>
	 * 	<li> <b>null</b> - ������ �� ����������, �.�. ������ ������ ���� �������� ������� </li>
	 * 	<li> <b>text</b> - the error presents, object is empty </li>
	 * </ul>
	 * */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * ���������� ����� ������, ��� ���� � ������� ��������� ���������������, �.�. �� ����� ����������� �� �������� ������� 
	 * @param errorMessage
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
