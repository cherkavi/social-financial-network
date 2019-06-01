package bonclub.office_private.web_service.db_function.wrap;

import java.sql.Date;

import bonclub.office_private.web_service.common_objects.UsersParent;

public class WrapPersonParameters extends Wrap{
	private int id;
	private String password;
	private Date registrationDate;
	private String codeCountry;
	private String adrZipCode;
	private String adrIdOblast;
	private String adrOblastName;
	private String adrDistrict;
	private String adrCity;
	private String adrStreet;
	private String adrHouse;
	private String adrCase;
	private String adrApartment;
	private String phoneHome;
	private String phoneMobile;
	private String email;
	private String profGroup;
	private String profGroupAnotherVariant;
	private Date birthDay;
	private String sex;
	
	
	/** Имя: */
	private String name;
	/** Фамилия: */
	private String surname;
	/** Отчество: */
	private String fatherName;
	private String profGroupId;
	
	
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
	 * @return the adrZipCode
	 */
	public String getAdrZipCode() {
		return adrZipCode;
	}
	/**
	 * @param adrZipCode the adrZipCode to set
	 */
	public void setAdrZipCode(String adrZipCode) {
		this.adrZipCode = adrZipCode;
	}
	/**
	 * @return the adrIdOblast
	 */
	public String getAdrIdOblast() {
		return adrIdOblast;
	}
	/**
	 * @param adrIdOblast the adrIdOblast to set
	 */
	public void setAdrIdOblast(String adrIdOblast) {
		this.adrIdOblast = adrIdOblast;
	}
	/**
	 * @return the adrDistrict
	 */
	public String getAdrDistrict() {
		return adrDistrict;
	}
	/**
	 * @param adrDistrict the adrDistrict to set
	 */
	public void setAdrDistrict(String adrDistrict) {
		this.adrDistrict = adrDistrict;
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
	 * @return the porfGroupAnotherVariant
	 */
	public String getProfGroupAnotherVariant() {
		return profGroupAnotherVariant;
	}
	/**
	 * @param porfGroupAnotherVariant the porfGroupAnotherVariant to set
	 */
	public void setProfGroupAnotherVariant(String porfGroupAnotherVariant) {
		this.profGroupAnotherVariant = porfGroupAnotherVariant;
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
	public Date getBirthDay() {
		return birthDay;
	}
	/**
	 * @param birthDay the birthDay to set
	 */
	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
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
	/** сформировать на основании данного объекта, который является синонимомо {@link UserParent} самого "синонима"*/
	public UsersParent getUsersParent() {
		UsersParent returnValue=new UsersParent();
		returnValue.setFlatNumber(this.getAdrApartment());
		returnValue.setHousing(this.getAdrCase());
		returnValue.setCity(this.getAdrCity());
		returnValue.setOblast(this.getAdrDistrict());
		returnValue.setHouse (this.getAdrHouse());
		returnValue.setOblastId(this.getAdrIdOblast());
		returnValue.setStreet(this.getAdrStreet());
		returnValue.setPostIndex(this.getAdrZipCode());
		returnValue.setCountryKod(this.getCodeCountry());
		returnValue.setEmail(this.getEmail());
		returnValue.setId(this.getId());
		returnValue.setPassword(this.getPassword());
		returnValue.setPhoneHome(this.getPhoneHome());
		returnValue.setPhoneMobile(this.getPhoneMobile());
		returnValue.setProf(this.getProfGroup());
		returnValue.setProfOther(this.getProfGroupAnotherVariant());
		returnValue.setRegistrationDate(this.getRegistrationDate());
		returnValue.setName(this.name);
		returnValue.setFatherName(this.fatherName);
		returnValue.setSurname(this.surname);
		returnValue.setBirthDate(this.birthDay);
		returnValue.setSex(this.sex);
		returnValue.setOblast(this.adrOblastName);
		returnValue.setProfId(this.profGroupId);
		// returnValue.set (this.getRegistrationDate());
		return returnValue;
	}
	public void setProfGroupId(String profGroupId) {
		this.profGroupId=profGroupId;
	}
	
	public String getProfGroupId(){
		return this.profGroupId;
	}
}
