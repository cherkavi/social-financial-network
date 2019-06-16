package bonclub.office_private.database.wrap;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
/** 
 * <table border="1">
 * 	<tr><th>Database</th> <th> POJO</th></tr>

<tr><td> 	ID	</td> <td>	kod	</td></tr>
<tr><td> 	LAST_ENTER	</td> <td>	lastEner	</td></tr>
<tr><td> 	BONCARD_NUMBER	</td> <td>	boncardNumber	</td></tr>
<tr><td> 	SECRET_ANSWER	</td> <td>	secretAnswer	</td></tr>
<tr><td> 	REGISTRATION_DATE	</td> <td>	registrationDate	</td></tr>
<tr><td> 	CREATE_TIME	</td> <td>	createTime	</td></tr>
<tr><td> 	CUSTOMER_LOGIN	</td> <td>	login	</td></tr>
<tr><td> 	CUSTOMER_PASSWORD	</td> <td>	password	</td></tr>
<tr><td> 	NICK	</td> <td>	nick	</td></tr>
<tr><td> 	FULL_NAME	</td> <td>	fullName	</td></tr>

 * </table>
 * 
 * 
 * */
@Entity
@Table(name="USERS")
public class Users implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name="generator",sequenceName="GEN_USERS_ID")	
	@GeneratedValue(strategy=GenerationType.AUTO,generator="generator")	
	@Column(name="ID")
	private int kod;

	@Column(name="LAST_ENTER")
	private Date lastEnter;
	
	@Column(name="BONCARD_NUMBER",length=30)
	private String boncardNumber;
	
	@Column(name="SECRET_ANSWER",length=255)
	private String secretAnswer;
	
	@Column(name="REGISTRATION_DATE")
	private Date registrationDate;
	
	@Column(name="CREATE_TIME")
	private Date createTime;
	
	@Column(name="CUSTOMER_LOGIN",length=50)
	private String login;
	
	@Column(name="CUSTOMER_PASSWORD",length=50)
	private String password;
	
	@Column(name="NICK",length=50)
	private String nick;
	
	@Column(name="FULL_NAME",length=255)
	private String fullName;
	
	@Transient
	/** поле для выделения пользователя в списке<br> 
	 * не используется в объекте 
	 * */
	private Boolean selected=false;

	
	
	
	public boolean equals(Object object){
		boolean return_value=false;
		if(object instanceof Users){
			return_value=((Users)object).getKod()==this.getKod();
		};
		return return_value;
	}
	
	public int hashCode(){
		return this.kod;
	}


	public int getKod() {
		return kod;
	}

	public void setKod(int kod) {
		this.kod = kod;
	}

	public Date getLastEnter() {
		return lastEnter;
	}

	public void setLastEnter(Date lastEnter) {
		this.lastEnter = lastEnter;
	}

	public String getBoncardNumber() {
		return boncardNumber;
	}

	public void setBoncardNumber(String boncardNumber) {
		this.boncardNumber = boncardNumber;
	}

	public String getSecretAnswer() {
		return secretAnswer;
	}

	public void setSecretAnswer(String secretAnswer) {
		this.secretAnswer = secretAnswer;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	
}
