package bonclub.office_private.web_service.common_objects;

import java.io.Serializable;
import java.util.Date;


/** Ответ центральной базы данных на введенный запрос {@link RegistrationRequest} системы OfficePrivate на получение новых данных о клиенте    */
public class RegistrationResponse  implements Serializable{
	private static final long serialVersionUID = 1664010852255176509L;
	
	/** уникальный идентификатор пользователя в масштабе удаленной базы */
	private int parentId;
	private String login;
	private String password;
	private Date dateRegistration;
	private String name;
	private String surname;
	private String fathername;
	private String errorMessage;
	
	/** Ответ центральной базы данных на введенный запрос {@link RegistrationRequest} системы OfficePrivate на получение новых данных о клиенте    */
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
	 * @return Логин
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
	 * @return Пароль
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
	 * @return Дата регистрации в системе 
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

	/** получить информацию об ошибке 
	 * @return 
	 * <ul>
	 * 	<li> <b>null</b> - ошибки не существует, т.е. объект должен быть наполнен данными </li>
	 * 	<li> <b>text</b> - the error presents, object is empty </li>
	 * </ul>
	 * */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * установить текст ошибки, все поля в объекте считаются анулированнымии, т.е. не будут рассмотрены на конечном объекте 
	 * @param errorMessage
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
