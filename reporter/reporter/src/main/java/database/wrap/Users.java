package database.wrap;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
/** 
 * <table border="1">
 * 	<tr><th>Database</th> <th> POJO</th></tr>

<tr><td> 	ID	</td> <td>	kod	</td></tr>
<tr><td> 	LAST_ENTER	</td> <td>	lastEner	</td></tr>
<tr><td> 	REGISTRATION_DATE	</td> <td>	registrationDate	</td></tr>
<tr><td> 	CREATE_TIME	</td> <td>	createTime	</td></tr>
<tr><td> 	NICK	</td> <td>	nick	</td></tr>
<tr><td> 	FULL_NAME	</td> <td>	fullName	</td></tr>
<tr><td> 	EMAIL	</td> <td>	email	</td></tr>
<tr><td> 	ID_PARENT	</td> <td>	idParent	</td></tr>

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

	@Column(name="CREATE_TIME")
	private Date createTime;
	
	@Column(name="NICK",length=50)
	private String nick;
	
	@Column(name="FULL_NAME",length=255)
	private String fullName;
	
	@Column(name="EMAIL",length=200)
	private String email;
	
	@Column(name="ID_PARENT")
	private Integer idParent;
	
	/** код страны */
	@Column(name="COUNTRY_KOD",length=20)
	private String countryKod;
	
	/** Имя: */
	@Column(name="NAME",length=100)
	private String name;
	
	/** Фамилия: */
	@Column(name="SURNAME",length=100)
	private String surname;
	
	/** Отчество: */
	@Column(name="FATHER_NAME",length=100)
	private String fatherName;
	
	/** Дата рождения: */
	@Column(name="BIRTH_DAY",length=30)
	private String birthDay;
	
	/** Дата рождения в виде java.util.Date */
	@Column(name="BIRTH_DATE")
	private Date birthDate;
	
	/** Пол: */
	@Column(name="SEX",length=15)
	private String sex;
	
	/** индекс */
	@Column(name="POST_INDEX",length=30)
	private String postIndex;
	
	/**  область */
	@Column(name="OBLAST",length=100)
	private String oblast;
	
	/** код области */
	@Column(name="OBLAST_ID",length=20)
	private String oblastId;
	
	/**  район */
	@Column(name="REGION",length=100)
	private String region;
	
	/**  город */
	@Column(name="CITY",length=100)
	private String city;
	
	/**  улица */
	@Column(name="STREET",length=100)
	private String street;
	
	/**  дом */
	@Column(name="HOME_NUMBER",length=30)
	private String house;
	
	/**  корпус */
	@Column(name="HOUSING",length=10)
	private String housing;
	
	/**  номер квартиры */
	@Column(name="FLAT_NUMBER",length=10)
	private String flatNumber;
	
	/** домашний телефон */
	@Column(name="PHONE_HOME",length=40)
	private String phoneHome; 
	
	/** мобильный телефон */
	@Column(name="PHONE_MOBILE",length=40)
	private String phoneMobile;
	
	/** профессия - текстовое представление  */
	@Column(name="PROF",length=40)
	private String prof; // cd_nat_prs_group
	
	/**уникальное название профессии из таблицы в локальной базе данных (nat_prs_group_all)
			select * from nat_prs_group_all where CD_NAT_PRS_GROUP='OTHER_VARIANT'*/
	@Column(name="PROF_ID")
	private String profId;
	
	/** другой вариант профессии - не из списка */
	@Column(name="PROF_OTHER",length=100)
	private String profOther;
	
	/** время последнего входа в кабинет */
	@Column(name="LAST_ENTER")
	private Date lastEnter;
	
	/** дата регистрации данного пользователя */
	@Column(name="REGISTRATION_DATE")
	private Date registrationDate;
	
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


	/** уникальный идентификатор данного пользователя */
	public int getKod() {
		return kod;
	}

	public void setKod(int kod) {
		this.kod = kod;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email.toLowerCase();
	}

	/**
	 * @return the idParent
	 */
	public Integer getIdParent() {
		return idParent;
	}

	/**
	 * @param idParent the idParent to set
	 */
	public void setIdParent(Integer idParent) {
		this.idParent = idParent;
	}

	/**
	 * @return the countryKod
	 */
	public String getCountryKod() {
		return countryKod;
	}

	/**
	 * @param countryKod the countryKod to set
	 */
	public void setCountryKod(String countryKod) {
		this.countryKod = countryKod;
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
	 * @return the fatherName
	 */
	public String getFatherName() {
		return fatherName;
	}

	/**
	 * @param fatherName the fatherName to set
	 */
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	/**
	 * @return the birthDay
	 */
	public String getBirthDay() {
		return birthDay;
	}

	/**
	 * @param birthDay the birthDay to set
	 */
	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}

	/**
	 * @return the birthDate
	 */
	public Date getBirthDate() {
		return birthDate;
	}

	/**
	 * @param birthDate the birthDate to set
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * @param sex the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * @return the postIndex
	 */
	public String getPostIndex() {
		return postIndex;
	}

	/**
	 * @param postIndex the postIndex to set
	 */
	public void setPostIndex(String postIndex) {
		this.postIndex = postIndex;
	}

	/**
	 * @return the oblast
	 */
	public String getOblast() {
		return oblast;
	}

	/**
	 * @param oblast the oblast to set
	 */
	public void setOblast(String oblast) {
		this.oblast = oblast;
	}

	/**
	 * @return the oblastId
	 */
	public String getOblastId() {
		return oblastId;
	}

	/**
	 * @param oblastId the oblastId to set
	 */
	public void setOblastId(String oblastId) {
		this.oblastId = oblastId;
	}

	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * @param region the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * @return the house
	 */
	public String getHouse() {
		return house;
	}

	/**
	 * @param house the house to set
	 */
	public void setHouse(String house) {
		this.house = house;
	}

	/**
	 * @return the housing
	 */
	public String getHousing() {
		return housing;
	}

	/**
	 * @param housing the housing to set
	 */
	public void setHousing(String housing) {
		this.housing = housing;
	}

	/**
	 * @return the flatNumber
	 */
	public String getFlatNumber() {
		return flatNumber;
	}

	/**
	 * @param flatNumber the flatNumber to set
	 */
	public void setFlatNumber(String flatNumber) {
		this.flatNumber = flatNumber;
	}

	/**
	 * @return the phoneHome
	 */
	public String getPhoneHome() {
		return phoneHome;
	}

	/**
	 * @param phoneHome the phoneHome to set
	 */
	public void setPhoneHome(String phoneHome) {
		this.phoneHome = phoneHome;
	}

	/**
	 * @return the phoneMobile
	 */
	public String getPhoneMobile() {
		return phoneMobile;
	}

	/**
	 * @param phoneMobile the phoneMobile to set
	 */
	public void setPhoneMobile(String phoneMobile) {
		this.phoneMobile = phoneMobile;
	}

	/**
	 * @return the prof
	 */
	public String getProf() {
		return prof;
	}

	/**
	 * @param prof the prof to set
	 */
	public void setProf(String prof) {
		this.prof = prof;
	}

	/**
	 * @return the profId
	 */
	public String getProfId() {
		return profId;
	}

	/**
	 * @param profId the profId to set
	 */
	public void setProfId(String profId) {
		this.profId = profId;
	}

	/**
	 * @return the profOther
	 */
	public String getProfOther() {
		return profOther;
	}

	/**
	 * @param profOther the profOther to set
	 */
	public void setProfOther(String profOther) {
		this.profOther = profOther;
	}

	/**
	 * @return the lastEnter
	 */
	public Date getLastEnter() {
		return lastEnter;
	}

	/**
	 * @param lastEnter the lastEnter to set
	 */
	public void setLastEnter(Date lastEnter) {
		this.lastEnter = lastEnter;
	}

	/**
	 * @return the registrationDate
	 */
	public Date getRegistrationDate() {
		return registrationDate;
	}

	/**
	 * @param registrationDate the registrationDate to set
	 */
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

}
