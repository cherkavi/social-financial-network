package bonclub.office_private.web_service.db_function.wrap;

import java.sql.Date;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import bonclub.office_private.web_service.common_objects.RegistrationResponse;
import bonclub.office_private.web_service.common_objects.UsersParent;

/** �����-������� ��� ������ � ������������, ������� �������� �� ���� ������ �� ��������� ����������� ��������  */
public class WrapPerson extends Wrap{
	/** ���������� ��� */
	private int id;
	/** ����� ���-����� */
	private String cdCard;
	/** ��������� ����� */
	private String cardState;
	/** ������ ��� ����� �� ������ */
	private String password;
	/** ���� ����������� ������� �� �������  */
	private Date registrationDate;
	
	/** �������� ����� ����� */
	private String cardSerialNumber;
	/** �� �������� ����� */
	private Integer idEmitentCard;
	/** �������� �������� ����� */
	private String nameEmitentCard;
	/** id ��������� ������� ����� */
	private Integer idPaymentSystem;
	/** �������� ��������� ������� ����� */
	private String namePaymentSystem;
	
	 /** ���� */
	private String cardStatus;
	/** ��������� ���-����� (����)*/
	private String cardCategory;
	/** ��������� ���-����� (������)*/
	private String discCategory;
    /** �������� ����� �� ����� (�������) */
	private int balCur;
    /** ��������� ����� �� ����� (�������) */
	private int balAcc;
	

	/** ��������� ����� �� ������ (�������) */
	private String balBonPer;
	/** ��������� ������ (�������) */
	private String balDiscPer;
	/** ��������� ���� �������� ����� � ��������� */
	private Date nextDateMov;
	/** ��������� ���� �������� ��������� */
	private Date nextDateCalc; 

    /** �������: */
	private String surname;
    /** ���: */
	private String name;
    /** ��������: */
	private String fatherName;
    /** ���� ��������: */
	private Date dateOfBirth;
    /** ��� ������ */
	private String codeCountry;
    /** ���: */
	private String sex;
    /** ������ */
	private String zipCode;
    /** ��� ������� */
	private String adrOblastId;
    /** ������� */
	private String adrOblastName; 
    /**  ����� */
	private String adrDiscrict;
    /**  ����� */
	private String adrCity;
    /**  ����� */
	private String adrStreet;
    /**  ��� */
	private String adrHouse;
    /**  ������ */
	private String adrCase;
    /**  ����� �������� */
	private String adrApartment;
    /** �������� ������� */
	private String phoneHome;
    /** ��������� ������� */
	private String phoneMobile;
    /** e-mail */
	private String email;
    /** ��������� ��������� ������� �� ���� ������ */
	private String profGroup;
    /** ��������� - ��������� �������������  */
	private String profGroupText;
    /** ������ ������� ��������� - �� �� ������ */
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
	
	/** @param ���� ����������� �� �������*/
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	/** ���� ����������� �� ������� */
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

	/** �������� �������� ����� �����  */
	public String getCardSerialNumber() {
		return cardSerialNumber;
	}
	
	/** ���������� �������� ����� �����  */
	public void setCardSerialNumber(String cardSerialNumber) {
		this.cardSerialNumber = cardSerialNumber;
	}
	
	/** �������� ��� �������� ����� */
	public Integer getIdEmitentCard() {
		return idEmitentCard;
	}
	
	/** ���������� ��� �������� ����� */
	public void setIdEmitentCard(Integer idEmitentCard) {
		this.idEmitentCard = idEmitentCard;
	}
	
	/** �������� �������� �������� �����  */
	public String getNameEmitentCard() {
		return nameEmitentCard;
	}
	
	/** ���������� �������� �������� ����� */
	public void setNameEmitentCard(String nameEmitentCard) {
		this.nameEmitentCard = nameEmitentCard;
	}

	/** �������� ID ��������� ������� */
	public Integer getIdPaymentSystem() {
		return idPaymentSystem;
	}
	/** ���������� ID ��������� �������  */
	public void setIdPaymentSystem(Integer idPaymentSystem) {
		this.idPaymentSystem = idPaymentSystem;
	}
	
	/** �������� ��� ��������� �������  */
	public String getNamePaymentSystem() {
		return namePaymentSystem;
	}
	
	/** ���������� ��� ��������� ������� */
	public void setNamePaymentSystem(String namePaymentSystem) {
		this.namePaymentSystem = namePaymentSystem;
	}



	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd");
	
	/** ����������� ���������� ������� ������� � {@link UsersParent} */
	public void copyTo(UsersParent returnValue) {
		/** ��������� ����� �� ������ (�������) */
		returnValue.setBalBonPer(this.getBalBonPer());
		/** ��������� ������ (�������) */
		returnValue.setBalDiscPer(this.getBalDiscPer());
		/** ��������� ���� �������� ����� � ��������� */
		try{
			returnValue.setNextDateMov(new java.util.Date(this.nextDateMov.getTime()));
		}catch(Exception ex){};
		/** ��������� ���� �������� ��������� */
		try{
			returnValue.setNextDateCalc(new java.util.Date(this.nextDateCalc.getTime()));
		}catch(Exception ex){};
		/** ���� */
		returnValue.setClub(this.cardStatus);
		/** ��������� ���-����� */
		returnValue.setCardState(this.cardState);
		/** �������� ����� �� ����� */
		returnValue.setBonAviable(Integer.toString(this.balCur));
		/** ��������� ����� �� ����� */
		returnValue.setBonStorage(Integer.toString(this.balAcc));
		// this.password
		try{
			returnValue.setBirthDate(new java.util.Date(this.dateOfBirth.getTime()));
		}catch(Exception ex){};
		try{
			returnValue.setBirthDay(sdf.format(returnValue.getBirthDate()));
		}catch(Exception ex){};
		
		/** ���������� ������������� ������������ */
		returnValue.setId(this.id);
		/** ������ ��� ������� � ������� */
		returnValue.setPassword(this.password);
		/** ��� ������ */
		returnValue.setCountryKod(this.codeCountry);
		// ���������� � ������������ private String fullName;
		/** ����� ���-����� */
		returnValue.setBoncardNumber(this.cdCard);
		/** ��������� ���-����� (����)*/
		returnValue.setBonCategory(this.cardCategory);
		/** ��������� ���-����� (������)*/
		returnValue.setDiscontCategory(this.discCategory);
		/** ���: */		
		returnValue.setName(this.name);
		/** �������: */
		returnValue.setSurname(surname);
		/** ��������: */
		returnValue.setFatherName(fatherName);
		/** ���: */
		returnValue.setSex(this.sex);
		/** e-mail */
		returnValue.setEmail(email);
		/** �������� ������� */
		returnValue.setPhoneHome(this.phoneHome);
		/** ��������� ������� */
		returnValue.setPhoneMobile(this.phoneMobile);
		/** ������ */
		returnValue.setPassword(this.password);
		/** ��������� ��������� ������� �� ���� ������ */
		returnValue.setProfId(this.profGroup);
		/** ��������� - ��������� �������������  */
		returnValue.setProf(this.profGroupText);
		/** ������ ������� ��������� - �� �� ������ */
		returnValue.setProfOther(this.profGroupOtherVariant);
		/** ���� ����������� ������� ������� */
		returnValue.setRegistrationDate(this.registrationDate);
		/** ����� ����������� ������ */
		try{
			// ��������� ����� �������������
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(new java.util.Date((new java.util.Date()).getTime()- this.registrationDate.getTime()));
			returnValue.setTimeUse(calendar);
		}catch(Exception ex){};
		
		/** ������ */
		returnValue.setPostIndex(this.zipCode);
		/**  ������� */
		returnValue.setOblast(this.adrOblastName);
		/** ��� ������� */
		returnValue.setOblastId(this.adrOblastId);
		/**  ����� */
		returnValue.setRegion(this.adrDiscrict);
		/**  ����� */
		returnValue.setCity(this.adrCity);
		/**  ����� */
		returnValue.setStreet(this.adrStreet);
		/**  ��� */
		returnValue.setHouse(this.adrHouse);
		/**  ������ */
		returnValue.setHousing(this.adrCase);
		/**  ����� �������� */
		returnValue.setFlatNumber(this.adrApartment);
		/** �������� ����� �����  */
		returnValue.setCardSerialNumber(this.getCardSerialNumber());
		/** ���������� ��� �������� �����  */
		returnValue.setIdEmitentCard(this.getIdEmitentCard());
		/** ��� �������� �����  */
		returnValue.setNameEmitentCard(this.getNameEmitentCard());
		/** ���������� ������������� ��������� ������� ����� */
		returnValue.setIdPaymentSystemCard(this.getIdPaymentSystem());
		/** �������� ��������� ������� ����� */
		returnValue.setNamePaymentSystemCard(this.getNamePaymentSystem());
		try{
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(new java.util.Date(this.registrationDate.getTime()));
			returnValue.setTimeUse(calendar);
		}catch(Exception ex){
			
		}
	}
}
