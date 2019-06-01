package bonclub.office_private.web_service.db_function.wrap;

import java.sql.Date;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import bonclub.office_private.web_service.common_objects.RegistrationResponse;
import bonclub.office_private.web_service.common_objects.UsersParent;

/** класс-обертка для данных о пользователе, которые получены из базы данных на основании выполненных процедур  */
public class WrapPerson extends Wrap{
	/** уникальный код */
	private int id;
	/** Номер бон-карты */
	private String cdCard;
	/** состояние карты */
	private String cardState;
	/** пароль для входа на ресурс */
	private String password;
	/** дата регистрации клиента на ресурсе  */
	private Date registrationDate;
	
	/** Серийный номер карты */
	private String cardSerialNumber;
	/** ИД эмитента карты */
	private Integer idEmitentCard;
	/** название эмитента карты */
	private String nameEmitentCard;
	/** id платежной системы карты */
	private Integer idPaymentSystem;
	/** название платежной системы карты */
	private String namePaymentSystem;
	
	 /** Клуб */
	private String cardStatus;
	/** Категория бон-карты (боны)*/
	private String cardCategory;
	/** Категория бон-карты (скидки)*/
	private String discCategory;
    /** Доступно бонов на карте (копейки) */
	private int balCur;
    /** Накоплено бонов на карте (копейки) */
	private int balAcc;
	

	/** Накоплено бонов за период (копейки) */
	private String balBonPer;
	/** Накоплено скидки (копейки) */
	private String balDiscPer;
	/** Следующая дата перевода бонов в доступные */
	private Date nextDateMov;
	/** Следующая дата перевода категорий */
	private Date nextDateCalc; 

    /** Фамилия: */
	private String surname;
    /** Имя: */
	private String name;
    /** Отчество: */
	private String fatherName;
    /** Дата рождения: */
	private Date dateOfBirth;
    /** код страны */
	private String codeCountry;
    /** Пол: */
	private String sex;
    /** индекс */
	private String zipCode;
    /** код области */
	private String adrOblastId;
    /** область */
	private String adrOblastName; 
    /**  район */
	private String adrDiscrict;
    /**  город */
	private String adrCity;
    /**  улица */
	private String adrStreet;
    /**  дом */
	private String adrHouse;
    /**  корпус */
	private String adrCase;
    /**  номер квартиры */
	private String adrApartment;
    /** домашний телефон */
	private String phoneHome;
    /** мобильный телефон */
	private String phoneMobile;
    /** e-mail */
	private String email;
    /** профессия текстовый акроним из базы данных */
	private String profGroup;
    /** профессия - текстовое представление  */
	private String profGroupText;
    /** другой вариант профессии - не из списка */
	private String profGroupOtherVariant;
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the cdCard
	 */
	public String getCdCard() {
		return cdCard;
	}
	/**
	 * @param cdCard the cdCard to set
	 */
	public void setCdCard(String cdCard) {
		this.cdCard = cdCard;
	}
	/**
	 * @return the cardStatus
	 */
	public String getCardStatus() {
		return cardStatus;
	}
	/**
	 * @param cardStatus the cardStatus to set
	 */
	public void setCardStatus(String cardStatus) {
		this.cardStatus = cardStatus;
	}
	/**
	 * @return the cardCategory
	 */
	public String getCardCategory() {
		return cardCategory;
	}
	/**
	 * @param cardCategory the cardCategory to set
	 */
	public void setCardCategory(String cardCategory) {
		this.cardCategory = cardCategory;
	}
	/**
	 * @return the discCategory
	 */
	public String getDiscCategory() {
		return discCategory;
	}
	/**
	 * @param discCategory the discCategory to set
	 */
	public void setDiscCategory(String discCategory) {
		this.discCategory = discCategory;
	}
	/**
	 * @return the balCur
	 */
	public int getBalCur() {
		return balCur;
	}
	/**
	 * @param balCur the balCur to set
	 */
	public void setBalCur(int balCur) {
		this.balCur = balCur;
	}
	/**
	 * @return the balAcc
	 */
	public int getBalAcc() {
		return balAcc;
	}
	/**
	 * @param balAcc the balAcc to set
	 */
	public void setBalAcc(int balAcc) {
		this.balAcc = balAcc;
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
	 * @return the dateOfBirth
	 */
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	/**
	 * @param dateOfBirth the dateOfBirth to set
	 */
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	/**
	 * @return the codeCountry
	 */
	public String getCodeCountry() {
		return codeCountry;
	}
	/**
	 * @param codeCountry the codeCountry to set
	 */
	public void setCodeCountry(String codeCountry) {
		this.codeCountry = codeCountry;
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
	 * @return the zipCode
	 */
	public String getZipCode() {
		return zipCode;
	}
	/**
	 * @param zipCode the zipCode to set
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	/**
	 * @return the adrOblastName
	 */
	public String getAdrOblastName() {
		return adrOblastName;
	}
	/**
	 * @param adrOblastName the adrOblastName to set
	 */
	public void setAdrOblastName(String adrOblastName) {
		this.adrOblastName = adrOblastName;
	}
	/**
	 * @return the adrDiscrict
	 */
	public String getAdrDiscrict() {
		return adrDiscrict;
	}
	/**
	 * @param adrDiscrict the adrDiscrict to set
	 */
	public void setAdrDiscrict(String adrDiscrict) {
		this.adrDiscrict = adrDiscrict;
	}
	/**
	 * @return the adrCity
	 */
	public String getAdrCity() {
		return adrCity;
	}
	/**
	 * @param adrCity the adrCity to set
	 */
	public void setAdrCity(String adrCity) {
		this.adrCity = adrCity;
	}
	/**
	 * @return the adrStreet
	 */
	public String getAdrStreet() {
		return adrStreet;
	}
	/**
	 * @param adrStreet the adrStreet to set
	 */
	public void setAdrStreet(String adrStreet) {
		this.adrStreet = adrStreet;
	}
	/**
	 * @return the adrHouse
	 */
	public String getAdrHouse() {
		return adrHouse;
	}
	/**
	 * @param adrHouse the adrHouse to set
	 */
	public void setAdrHouse(String adrHouse) {
		this.adrHouse = adrHouse;
	}
	/**
	 * @return the adrCase
	 */
	public String getAdrCase() {
		return adrCase;
	}
	/**
	 * @param adrCase the adrCase to set
	 */
	public void setAdrCase(String adrCase) {
		this.adrCase = adrCase;
	}
	/**
	 * @return the adrApartment
	 */
	public String getAdrApartment() {
		return adrApartment;
	}
	/**
	 * @param adrApartment the adrApartment to set
	 */
	public void setAdrApartment(String adrApartment) {
		this.adrApartment = adrApartment;
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
	 * @return the profGroup
	 */
	public String getProfGroup() {
		return profGroup;
	}
	/**
	 * @param profGroup the profGroup to set
	 */
	public void setProfGroup(String profGroup) {
		this.profGroup = profGroup;
	}
	/**
	 * @return the profGroupText
	 */
	public String getProfGroupText() {
		return profGroupText;
	}
	/**
	 * @param profGroupText the profGroupText to set
	 */
	public void setProfGroupText(String profGroupText) {
		this.profGroupText = profGroupText;
	}
	/**
	 * @return the profGroupOtherVariant
	 */
	public String getProfGroupOtherVariant() {
		return profGroupOtherVariant;
	}
	/**
	 * @param profGroupOtherVariant the profGroupOtherVariant to set
	 * 
	 */
	public void setProfGroupOtherVariant(String profGroupOtherVariant) {
		this.profGroupOtherVariant = profGroupOtherVariant;
	}
	/**
	 * @return the adrOblastId
	 */
	public String getAdrOblastId() {
		return adrOblastId;
	}
	/**
	 * @param adrOblastId the adrOblastId to set
	 */
	public void setAdrOblastId(String adrOblastId) {
		this.adrOblastId = adrOblastId;
	}
	public void copyTo(RegistrationResponse returnValue) {
		returnValue.setDateRegistration(this.getRegistrationDate());
		returnValue.setName(this.getName());
		returnValue.setSurname(this.getSurname());
		returnValue.setFathername(this.getFatherName());
		returnValue.setLogin(this.getCdCard());
		returnValue.setPassword(this.getPassword());
		returnValue.setParentId(this.getId());
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
	
	/** @param дата регистрации на ресурсе*/
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	/** дата регистрации на ресурсе */
	public Date getRegistrationDate() {
		return registrationDate;
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
	
	/** получить название эмитента карты  */
	public String getNameEmitentCard() {
		return nameEmitentCard;
	}
	
	/** установить название эмитента карты */
	public void setNameEmitentCard(String nameEmitentCard) {
		this.nameEmitentCard = nameEmitentCard;
	}

	/** получить ID платежной системы */
	public Integer getIdPaymentSystem() {
		return idPaymentSystem;
	}
	/** установить ID платежной системы  */
	public void setIdPaymentSystem(Integer idPaymentSystem) {
		this.idPaymentSystem = idPaymentSystem;
	}
	
	/** получить имя платежной системы  */
	public String getNamePaymentSystem() {
		return namePaymentSystem;
	}
	
	/** установить имя платежной системы */
	public void setNamePaymentSystem(String namePaymentSystem) {
		this.namePaymentSystem = namePaymentSystem;
	}



	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd");
	
	/** скопировать содержимое данного объекта в {@link UsersParent} */
	public void copyTo(UsersParent returnValue) {
		/** Накоплено бонов за период (копейки) */
		returnValue.setBalBonPer(this.getBalBonPer());
		/** Накоплено скидки (копейки) */
		returnValue.setBalDiscPer(this.getBalDiscPer());
		/** Следующая дата перевода бонов в доступные */
		try{
			returnValue.setNextDateMov(new java.util.Date(this.nextDateMov.getTime()));
		}catch(Exception ex){};
		/** Следующая дата перевода категорий */
		try{
			returnValue.setNextDateCalc(new java.util.Date(this.nextDateCalc.getTime()));
		}catch(Exception ex){};
		/** Клуб */
		returnValue.setClub(this.cardStatus);
		/** состояние Бон-Карты */
		returnValue.setCardState(this.cardState);
		/** Доступно бонов на карте */
		returnValue.setBonAviable(Integer.toString(this.balCur));
		/** Накоплено бонов на карте */
		returnValue.setBonStorage(Integer.toString(this.balAcc));
		// this.password
		try{
			returnValue.setBirthDate(new java.util.Date(this.dateOfBirth.getTime()));
		}catch(Exception ex){};
		try{
			returnValue.setBirthDay(sdf.format(returnValue.getBirthDate()));
		}catch(Exception ex){};
		
		/** уникальный идентификатор пользователя */
		returnValue.setId(this.id);
		/** пароль для доступа к ресурсу */
		returnValue.setPassword(this.password);
		/** код страны */
		returnValue.setCountryKod(this.codeCountry);
		// информация о пользователе private String fullName;
		/** Номер бон-карты */
		returnValue.setBoncardNumber(this.cdCard);
		/** Категория бон-карты (боны)*/
		returnValue.setBonCategory(this.cardCategory);
		/** Категория бон-карты (скидки)*/
		returnValue.setDiscontCategory(this.discCategory);
		/** Имя: */		
		returnValue.setName(this.name);
		/** Фамилия: */
		returnValue.setSurname(surname);
		/** Отчество: */
		returnValue.setFatherName(fatherName);
		/** Пол: */
		returnValue.setSex(this.sex);
		/** e-mail */
		returnValue.setEmail(email);
		/** домашний телефон */
		returnValue.setPhoneHome(this.phoneHome);
		/** мобильный телефон */
		returnValue.setPhoneMobile(this.phoneMobile);
		/** пароль */
		returnValue.setPassword(this.password);
		/** профессия текстовый акроним из базы данных */
		returnValue.setProfId(this.profGroup);
		/** профессия - текстовое представление  */
		returnValue.setProf(this.profGroupText);
		/** другой вариант профессии - не из списка */
		returnValue.setProfOther(this.profGroupOtherVariant);
		/** дата регистрации данного объекта */
		returnValue.setRegistrationDate(this.registrationDate);
		/** Время пользования картой */
		try{
			// вычисляем время использования
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(new java.util.Date((new java.util.Date()).getTime()- this.registrationDate.getTime()));
			returnValue.setTimeUse(calendar);
		}catch(Exception ex){};
		
		/** индекс */
		returnValue.setPostIndex(this.zipCode);
		/**  область */
		returnValue.setOblast(this.adrOblastName);
		/** код области */
		returnValue.setOblastId(this.adrOblastId);
		/**  район */
		returnValue.setRegion(this.adrDiscrict);
		/**  город */
		returnValue.setCity(this.adrCity);
		/**  улица */
		returnValue.setStreet(this.adrStreet);
		/**  дом */
		returnValue.setHouse(this.adrHouse);
		/**  корпус */
		returnValue.setHousing(this.adrCase);
		/**  номер квартиры */
		returnValue.setFlatNumber(this.adrApartment);
		/** серийный номер карты  */
		returnValue.setCardSerialNumber(this.getCardSerialNumber());
		/** уникальный код эмитента карты  */
		returnValue.setIdEmitentCard(this.getIdEmitentCard());
		/** имя эмитента карты  */
		returnValue.setNameEmitentCard(this.getNameEmitentCard());
		/** уникальный идентификатор платежной системы карты */
		returnValue.setIdPaymentSystemCard(this.getIdPaymentSystem());
		/** название платежной системы карты */
		returnValue.setNamePaymentSystemCard(this.getNamePaymentSystem());
		try{
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(new java.util.Date(this.registrationDate.getTime()));
			returnValue.setTimeUse(calendar);
		}catch(Exception ex){
			
		}
	}
}
