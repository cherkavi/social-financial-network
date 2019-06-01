package bonclub.office_private.web_service.common_objects;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/** транспортный объект для передачи данных по клиенту между сервером Oracle и клиентом WebServices {@link bonclub.office_private.database.wrap.UsersFromParentDatabase}*/
public class UsersParent  implements Serializable{
	private static final long serialVersionUID = 1861560070331955993L;
	
	/** уникальный идентификатор пользователя */
	private Integer id;
	/** пароль для доступа к ресурсу */
	private String password;
	/** код страны */
	private String countryKod;
	/** информация о пользователе */
	private String fullName;
	/** Номер бон-карты */
	private String boncardNumber;
	/** Клуб */
	private String club;    
	/** состояние карты */
	private String cardState;
	/** Категория бон-карты (боны)*/
	private String bonCategory;     
	/** Категория бон-карты (скидки)*/
	private String discontCategory; 
	/** Доступно бонов на карте */
	private String bonAviable;      
	/** Накоплено бонов на карте */
	private String bonStorage;      
	/** Время пользования картой */
	private Calendar timeUse;       
	/** Имя: */
	private String name;
	/** Фамилия: */
	private String surname;
	/** Отчество: */
	private String fatherName;
	/** Дата рождения: */
	private String birthDay;
	/** Дата рождения в виде java.util.Date */
	private Date birthDate;
	/** Пол: */
	private String sex;
	/** индекс */
	private String postIndex; // FACT_ADR_ZIP_CODE
	/**  область */
	private String oblast; // FACT_ADR_NAME_OBLAST
	/** код области */
	private String oblastId;
	/**  район */
	private String region; // FACT_ADR_DISTRICT
	/**  город */
	private String city; // FACT_ADR_CITY
	/**  улица */
	private String street; // FACT_ADR_STREET
	/**  дом */
	private String house; // FACT_ADR_HOUSE
	/**  корпус */
	private String housing; // FACT_ADR_CASE
	/**  номер квартиры */
	private String flatNumber; // FACT_ADR_APARTMENT
	/** домашний телефон */
	private String phoneHome; // PHONE_HOME 
	/** мобильный телефон */
	private String phoneMobile; // PHONE_MOBILE
	/** e-mail */
	private String email; // EMAIL
	/** профессия - текстовое представление  */
	private String prof; // cd_nat_prs_group
	/** профессия текстовый акроним из базы данных */
	private String profId;
	/** другой вариант профессии - не из списка */
	private String profOther;
	/** дата регистрации данного объекта */
	private Date registrationDate;
	
	/** Накоплено бонов за период (копейки) */
	private String balBonPer;
	/** Накоплено скидки (копейки) */
	private String balDiscPer;
	/** Следующая дата перевода бонов в доступные */
	private Date nextDateMov;
	/** Следующая дата перевода категорий */
	private Date nextDateCalc; 
	
	/** серийный номер карты */
	private String cardSerialNumber;
	/** код эмитента карты */
	private Integer idEmitentCard;
	/** наименование эмитента карты */
	private String nameEmitentCard;
	/** код платежной системы */
	private Integer idPaymentSystemCard;
	/** наименование платежной системы */
	private String namePaymentSystemCard;
	/** текст ошибки, который был выявлен при наполнении/получении объекта  */
	private String errorMessage;
	
	
	/**  транспортный объект для передачи данных по клиенту между сервером Oracle и клиентом WebServices */
	public UsersParent(){
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
	 * @return the boncardNumber
	 */
	public String getBoncardNumber() {
		return boncardNumber;
	}

	/**
	 * @param boncardNumber the boncardNumber to set
	 */
	public void setBoncardNumber(String boncardNumber) {
		this.boncardNumber = boncardNumber;
	}

	/**
	 * @return the club
	 */
	public String getClub() {
		return club;
	}

	/**
	 * @param club the club to set
	 */
	public void setClub(String club) {
		this.club = club;
	}

	/**
	 * @return the bonCategory
	 */
	public String getBonCategory() {
		return bonCategory;
	}

	/**
	 * @param bonCategory the bonCategory to set
	 */
	public void setBonCategory(String bonCategory) {
		this.bonCategory = bonCategory;
	}

	/**
	 * @return the discontCategory
	 */
	public String getDiscontCategory() {
		return discontCategory;
	}

	/**
	 * @param discontCategory the discontCategory to set
	 */
	public void setDiscontCategory(String discontCategory) {
		this.discontCategory = discontCategory;
	}

	/**
	 * @return the bonAviable
	 */
	public String getBonAviable() {
		return bonAviable;
	}

	/**
	 * @param bonAviable the bonAviable to set
	 */
	public void setBonAviable(String bonAviable) {
		this.bonAviable = bonAviable;
	}

	/**
	 * @return the bonStorage
	 */
	public String getBonStorage() {
		return bonStorage;
	}

	/**
	 * @param bonStorage the bonStorage to set
	 */
	public void setBonStorage(String bonStorage) {
		this.bonStorage = bonStorage;
	}

	/**
	 * @return the timeUse
	 */
	public Calendar getTimeUse() {
		return timeUse;
	}

	/**
	 * @param timeUse the timeUse to set
	 */
	public void setTimeUse(Calendar timeUse) {
		this.timeUse = timeUse;
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
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
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
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	};

	@Override
	public String toString(){
		StringBuffer returnValue=new StringBuffer();
		returnValue.append("\nId:"+id);
		returnValue.append("\ncountryKod:"+countryKod);
		returnValue.append("\nfullName:"+fullName);		
		returnValue.append("\nboncardNumber:"+boncardNumber);		
		returnValue.append("\nclub:"+club);		
		returnValue.append("\nbonCategory:"+bonCategory);		
		returnValue.append("\ndiscontCategory:"+discontCategory);		
		returnValue.append("\nbonAviable:"+bonAviable);		
		returnValue.append("\nbonStorage:"+bonStorage);		
		returnValue.append("\nname:"+name);		
		returnValue.append("\nsurname:"+surname);		
		returnValue.append("\nfatherName:"+fatherName);		
		returnValue.append("\nbirthDay:"+birthDay);		
		returnValue.append("\nsex:"+sex);		
		returnValue.append("\npostIndex:"+postIndex);		
		returnValue.append("\noblast:"+oblast);		
		returnValue.append("\noblastId:"+oblastId);		
		returnValue.append("\nregion:"+region);		
		returnValue.append("\ncity:"+city);		
		returnValue.append("\nstreet:"+street);		
		returnValue.append("\nhouse:"+house);		
		returnValue.append("\nhousing:"+housing);		
		returnValue.append("\nflatNumber:"+flatNumber);		
		returnValue.append("\nphoneHome:"+phoneHome);		
		returnValue.append("\nphoneMobile:"+phoneMobile);		
		returnValue.append("\nemail:"+email);		
		returnValue.append("\nprof:"+prof);		
		returnValue.append("\nprofId:"+profId);		
		returnValue.append("\nprofOther:"+profOther);		
		//private Date birthDate;
		//private Calendar timeUse;       
		return returnValue.toString();
	}

	/**
	 * @return the password
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

	/**
	 * @return the cardState
	 */
	public String getCardState() {
		return cardState;
	}

	/**
	 * @param cardState the cardState to set
	 */
	public void setCardState(String cardState) {
		this.cardState = cardState;
	}

	/**
	 * @return the balConPer
	 */
	public String getBalBonPer() {
		return balBonPer;
	}

	/**
	 * @param balConPer the balConPer to set
	 */
	public void setBalBonPer(String balBonPer) {
		this.balBonPer = balBonPer;
	}

	/**
	 * @return the balDiscPer
	 */
	public String getBalDiscPer() {
		return balDiscPer;
	}

	/**
	 * @param balDiscPer the balDiscPer to set
	 */
	public void setBalDiscPer(String balDiscPer) {
		this.balDiscPer = balDiscPer;
	}

	/**
	 * @return the nextDateMov
	 */
	public Date getNextDateMov() {
		return nextDateMov;
	}

	/**
	 * @param nextDateMov the nextDateMov to set
	 */
	public void setNextDateMov(Date nextDateMov) {
		this.nextDateMov = nextDateMov;
	}

	/**
	 * @return the nextDateCalc
	 */
	public Date getNextDateCalc() {
		return nextDateCalc;
	}

	/**
	 * @param nextDateCalc the nextDateCalc to set
	 */
	public void setNextDateCalc(Date nextDateCalc) {
		this.nextDateCalc = nextDateCalc;
	}

	/** получить серийный номер карты  */
	public String getCardSerialNumber() {
		return cardSerialNumber;
	}
	
	/** установить серийный номер карты  */
	public void setCardSerialNumber(String cardSerialNumber) {
		this.cardSerialNumber = cardSerialNumber;
	}
	
	/** получить код эмитента карты */
	public Integer getIdEmitentCard() {
		return idEmitentCard;
	}
	
	/** установить код эмитента карты */
	public void setIdEmitentCard(Integer idEmitentCard) {
		this.idEmitentCard = idEmitentCard;
	}
	
	/** получить код платежной системы */
	public Integer getIdPaymentSystemCard() {
		return idPaymentSystemCard;
	}
	
	/** установить код платежной системы */
	public void setIdPaymentSystemCard(Integer idPaySystemCard) {
		this.idPaymentSystemCard = idPaySystemCard;
	}

	/** получить имя эмитента карты */
	public String getNameEmitentCard() {
		return nameEmitentCard;
	}

	/** установить имя эмитента карты  */
	public void setNameEmitentCard(String nameEmitentCard) {
		this.nameEmitentCard = nameEmitentCard;
	}

	/** получить имя платежной системы */
	public String getNamePaymentSystemCard() {
		return namePaymentSystemCard;
	}

	/** установить имя платежной системы */
	public void setNamePaymentSystemCard(String namePaymentSystemCard) {
		this.namePaymentSystemCard = namePaymentSystemCard;
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
