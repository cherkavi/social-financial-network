package bonclub.office_private.database.wrap;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;

public class UsersParent implements Serializable{
	private final static long serialVersionUID=1L;
	private final static String emptyString="";
	public final static String keyProfOther="OTHER_VARIANT";
	private String query;
	/** ключевое поле, которое четко идентифицирует пользователя в базе данных 
	 * <br>
	 * <b>id_nat_prs</b>
	 * */
	private Integer id; //
	/** код страны */
	private String countryKod;
	/** информация о пользователе */
	private String fullName;
	/** Номер бон-карты */
	private String boncardNumber;
	/** Клуб */
	private String club;            // bc_admin.VC_CLUB_CARD_CLUB_ALL.NAME_CARD_STATUS
	/** Категория бон-карты (боны)*/
	private String bonCategory;     // bc_admin.VC_CLUB_CARD_CLUB_ALL.NAME_BON_CATEGORY
	/** Категория бон-карты (скидки)*/
	private String discontCategory; // bc_admin.VC_CLUB_CARD_CLUB_ALL.NAME_DISC_CATEGORY
	/** Доступно бонов на карте */
	private String bonAviable;      // bc_admin.VC_CLUB_CARD_CLUB_ALL.BAL_CUR_FMRT
	/** Накоплено бонов на карте */
	private String bonStorage;      // bc_admin.VC_CLUB_CARD_CLUB_ALL.BAL_ACC_FMRT
	/** Время пользования картой */
	private Calendar timeUse;         // bc_admin.VC_CLUB_CARD_CLUB_ALL.DATE_OPEN - дата открытия, нужно отнять от текущей даты
	
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
	
	/** объект-обертка для отображения информации о клиенте из родительской базы данных */
	public UsersParent(){
		StringBuffer tempQuery=new StringBuffer();		
		tempQuery.append("select ");
		tempQuery.append(" vc_nat.id_nat_prs id, \n");
		tempQuery.append(" vc_nat.fact_code_country code_country, \n");
		tempQuery.append(" vc_nat.full_name full_name, \n"); 
		tempQuery.append(" vc_club.cd_card1 boncard_number, \n");
		tempQuery.append(" vc_club.NAME_CARD_STATUS card_status, \n"); 
		tempQuery.append(" vc_club.NAME_BON_CATEGORY name_bon_category, \n");
		tempQuery.append(" vc_club.NAME_DISC_CATEGORY name_disc_category, \n");
		tempQuery.append(" vc_club.BAL_CUR_FRMT bal_cur, \n");
		tempQuery.append(" vc_club.BAL_ACC_FRMT bal_acc, \n");
		tempQuery.append(" vc_club.BAL_ACC_FRMT bal_acc, \n");
		tempQuery.append(" vc_nat.surname surname, \n");
		tempQuery.append(" vc_nat.name name, \n");
		tempQuery.append(" vc_nat.patronymic patronymic, \n");
		tempQuery.append(" vc_nat.date_of_birth_frmt date_of_birth_frmt, \n");
		tempQuery.append(" vc_nat.date_of_birth date_of_birth, \n");
		tempQuery.append(" vc_nat.sex_tsl sex_tsl, \n");
		tempQuery.append(" vc_nat.FACT_ADR_ZIP_CODE FACT_ADR_ZIP_CODE, \n");
		tempQuery.append(" vc_nat.FACT_ADR_NAME_OBLAST FACT_ADR_NAME_OBLAST, \n");
		tempQuery.append(" vc_nat.FACT_ADR_ID_OBLAST FACT_ADR_ID_OBLAST, \n");		
		tempQuery.append(" vc_nat.FACT_ADR_DISTRICT FACT_ADR_DISTRICT, \n");
		tempQuery.append(" vc_nat.FACT_ADR_CITY FACT_ADR_CITY, \n");
		tempQuery.append(" vc_nat.FACT_ADR_STREET FACT_ADR_STREET, \n");
		tempQuery.append(" vc_nat.FACT_ADR_HOUSE FACT_ADR_HOUSE, \n");
		tempQuery.append(" vc_nat.FACT_ADR_CASE FACT_ADR_CASE, \n");
		tempQuery.append(" vc_nat.FACT_ADR_APARTMENT FACT_ADR_APARTMENT, \n");
		tempQuery.append(" vc_nat.PHONE_HOME PHONE_HOME, \n");
		tempQuery.append(" vc_nat.PHONE_MOBILE PHONE_MOBILE, \n");
		tempQuery.append(" vc_nat.EMAIL EMAIL, \n");
		tempQuery.append(" vc_nat.cd_nat_prs_group cd_nat_prs_group, \n");
		tempQuery.append(" vc_nat.NAME_OTHER_VARIANT_GROUP NAME_OTHER_VARIANT_GROUP, \n");
		tempQuery.append(" \n");
		tempQuery.append(" vc_club.DATE_OPEN date_open \n");
		tempQuery.append("from bc_admin.vc_nat_prs_club_all vc_nat \n");
		tempQuery.append("inner join bc_admin.vc_club_card_club_all vc_club on vc_club.cd_card1 = '?' \n"); 
		tempQuery.append(" and vc_nat.ID_NAT_PRS=vc_club.id_nat_prs \n");
		//System.out.println(tempQuery.toString());
		this.query=tempQuery.toString();
	};
/*	
	this.surname=getEmptyStringIfNull(rs.getString("surname"));
	this.name=getEmptyStringIfNull(rs.getString("name"));
	this.fatherName=getEmptyStringIfNull(rs.getString("patronymic"));
	this.birthDay=rs.getString("date_of_birth_frmt"); 
	this.sex=rs.getString("sex_tsl");
*/	
	
	/** очистить объект */
	private void clear(){
		this.fullName=emptyString;
		this.boncardNumber=emptyString;
		this.countryKod=emptyString;
		this.id=0;
		this.fullName=emptyString;
		this.boncardNumber=emptyString;
		this.club=emptyString;            
		this.bonCategory=emptyString;     
		this.discontCategory=emptyString; 
		this.bonAviable=emptyString;      
		this.bonStorage=emptyString;      
		this.timeUse=null;
		this.name=emptyString;
		this.surname=emptyString;
		this.fatherName=emptyString;
		this.birthDay=emptyString;
		birthDate=null;
		this.sex=emptyString;
		this.postIndex=emptyString; 
		this.oblast=emptyString; 
		this.region=emptyString; 
		this.city=emptyString; 
		this.street=emptyString; 
		this.house=emptyString; 
		this.housing=emptyString; 
		this.flatNumber=emptyString; 
		this.phoneHome=emptyString;  
		this.phoneMobile=emptyString; 
		this.email=emptyString; 
		this.prof=emptyString; 
		this.profId=emptyString;
		this.profOther=emptyString;
	}
	
	/** наполнить данными объект, на основании Connection 
	 * @param connection - соединение с базой данных 
	 * @param boncardNumber - номер бонкарты
	 * @throws Exception - исключение, которое выбрасывается в случае неудачного считывания данных 
	 */
	public void fillData(Connection connection, String boncardNumber) throws Exception{
		this.clear();
		//System.out.println("query before: "+query);
		//System.out.println(">>>query after: "+query.replaceFirst("\\?", boncardNumber));
		ResultSet rs=null;
		ResultSet rsProf=null;
		try{
			rs=connection.createStatement().executeQuery(query.replaceFirst("\\?", boncardNumber));
			if(rs.next()){
				this.id=rs.getInt("id");
				this.countryKod=rs.getString("code_country");
				this.fullName=rs.getString("full_name");
				this.boncardNumber=rs.getString("boncard_number");
				this.club=getEmptyStringIfNull(rs.getString("card_status"));            // bc_admin.VC_CLUB_CARD_CLUB_ALL.NAME_CARD_STATUS
				/** Категория бон-карты (боны)*/
				this.bonCategory=getEmptyStringIfNull(rs.getString("name_bon_category"));     // bc_admin.VC_CLUB_CARD_CLUB_ALL.NAME_BON_CATEGORY
				/** Категория бон-карты (скидки)*/
				this.discontCategory=getEmptyStringIfNull(rs.getString("name_disc_category")); // bc_admin.VC_CLUB_CARD_CLUB_ALL.NAME_DISC_CATEGORY
				/** Доступно бонов на карте */
				this.bonAviable=getEmptyStringIfNull(rs.getString("bal_cur"));      // bc_admin.VC_CLUB_CARD_CLUB_ALL.BAL_CUR_FMRT
				/** Накоплено бонов на карте */
				this.bonStorage=getEmptyStringIfNull(rs.getString("bal_acc"));      // bc_admin.VC_CLUB_CARD_CLUB_ALL.BAL_ACC_FMRT
				/** фамилия */
				this.surname=getEmptyStringIfNull(rs.getString("surname"));
				/** имя */
				this.name=getEmptyStringIfNull(rs.getString("name"));
				/** Отчество */
				this.fatherName=getEmptyStringIfNull(rs.getString("patronymic"));
				/** дата рождения */
				this.birthDay=rs.getString("date_of_birth_frmt"); 
				/** пол */
				this.sex=getEmptyStringIfNull(rs.getString("sex_tsl"));
				this.postIndex=getEmptyStringIfNull(rs.getString("FACT_ADR_ZIP_CODE"));
				this.oblast=getEmptyStringIfNull(rs.getString("FACT_ADR_NAME_OBLAST"));
				this.oblastId=rs.getString("FACT_ADR_ID_OBLAST");
				this.region=getEmptyStringIfNull(rs.getString("FACT_ADR_DISTRICT"));
				this.city=getEmptyStringIfNull(rs.getString("FACT_ADR_CITY"));
				this.street=getEmptyStringIfNull(rs.getString("FACT_ADR_STREET"));
				this.house=getEmptyStringIfNull(rs.getString("FACT_ADR_HOUSE"));
				this.housing=getEmptyStringIfNull(rs.getString("FACT_ADR_CASE"));
				this.flatNumber=getEmptyStringIfNull(rs.getString("FACT_ADR_APARTMENT"));
				this.phoneHome=getEmptyStringIfNull(rs.getString("PHONE_HOME")); 
				this.phoneMobile=getEmptyStringIfNull(rs.getString("PHONE_MOBILE"));
				this.email=getEmptyStringIfNull(rs.getString("EMAIL"));
				this.profId=rs.getString("cd_nat_prs_group");
				this.profOther=rs.getString("NAME_OTHER_VARIANT_GROUP");

				if(this.isProfOther()){
					this.prof=getEmptyStringIfNull(rs.getString("NAME_OTHER_VARIANT_GROUP"));
				}else{
					rsProf=connection.createStatement().executeQuery("select NAME_NAT_PRS_GROUP from bc_admin.VC_NAT_PRS_GROUP_ALL where cd_nat_prs_group = '"+this.profId+"'");
					if(rsProf.next()){
						this.prof=rsProf.getString(1);
					}else{
						this.prof=emptyString;
					}
				}
				java.sql.Date dateOpen=rs.getDate("date_open");
				this.birthDate=this.getDateUtil(rs.getDate("date_of_birth"));
				Calendar calendar=Calendar.getInstance();
				calendar.setTime(new java.util.Date(new java.util.Date().getTime()-dateOpen.getTime()));
				this.timeUse=calendar;
			};
		}finally{
			try{
				rs.close();
			}catch(Exception ex){};
			try{
				rsProf.close();
			}catch(Exception ex){};
		}
	}

	/** конвертировать {@see java.sql.Date}в java.util.Date */
	private java.util.Date getDateUtil(java.sql.Date sqlDate){
		try{
			return new java.util.Date(sqlDate.getTime());
		}catch(Exception ex){
			return null;
		}
	}
	
	/** получить пустую строку вместо null*/
	private String getEmptyStringIfNull(String value){
		return (value==null)?emptyString:value;
	}
	
	
	/**
	 * @return Полное имя ( фамилия и имя)
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @return Номер бон-карты
	 */
	public String getBoncardNumber() {
		return boncardNumber;
	}

	/**
	 * @return Название клуба
	 */
	public String getClub() {
		return club;
	}

	/**
	 * @return Категория бон-карты (боны)
	 */
	public String getBonCategory() {
		return bonCategory;
	}

	/**
	 * @return категория бон-карты (скидки) 
	 */
	public String getDiscontCategory() {
		return discontCategory;
	}

	/**
	 * @return доступные боны средства на бон-карте 
	 */
	public String getBonAviable() {
		return bonAviable;
	}

	/**
	 * @return накопленные боны 
	 */
	public String getBonStorage() {
		return bonStorage;
	}

	/**
	 * @return Calendar (get(Calendar.YEAR),get(Calendar.MONTH),get(Calendar.DAY_OF_MONTH) - продолжительность использования карты )
	 */
	public Calendar getTimeUse() {
		return timeUse;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}
	/**
	 * @return the fatherName
	 */
	public String getFatherName() {
		return fatherName;
	}
	/**
	 * @return the birthDay
	 */
	public String getBirthDay() {
		return birthDay;
	}
	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}
	/**
	 * @return the postIndex
	 */
	public String getPostIndex() {
		return postIndex;
	}
	/**
	 * @return the oblast
	 */
	public String getOblast() {
		return oblast;
	}
	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}
	/**
	 * @return the house
	 */
	public String getHouse() {
		return house;
	}
	/**
	 * @return the housing
	 */
	public String getHousing() {
		return housing;
	}
	/**
	 * @return the flatNumber
	 */
	public String getFlatNumber() {
		return flatNumber;
	}
	/**
	 * @return the phoneHome
	 */
	public String getPhoneHome() {
		return phoneHome;
	}
	/**
	 * @return the phoneMobile
	 */
	public String getPhoneMobile() {
		return phoneMobile;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * 
	 * @return Получить текстовое описание профессии 
	 */
	public String getProf() {
		return prof;
	}
	
	/** получить уникальный идентификатор из базы данных по указанной профессии */
	public String getProfId(){
		return this.profId;
	}

	/** @return true - если профессия выбрана не из списка */
	public boolean isProfOther(){
		return this.profId.equals(keyProfOther);
	}
	
	/** получить строку с "Другим вариантом" заполненным пользователем */
	public String getProfOther(){
		return this.profOther;
	}
	
	/** 
	 * получить дату рождения держателя карты в виде {@link java.util.Date java.util.Date}
	 * 
	 * */
	public Date getBirthDate(){
		return birthDate;
	}

	String updateQuery;
	{
		StringBuffer query=new StringBuffer();
		query.append(" {? = call bc_admin.pack_ui_office.update_nat_prs(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)} ");
		/*query.append("	     p_id_nat_prs		\n");
		query.append("	    ,p_fact_code_country		\n");
		query.append("	    ,p_fact_adr_zip_code		\n");
		query.append("	    ,p_fact_adr_id_oblast		\n");
		query.append("	    ,p_fact_adr_district		\n");
		query.append("	    ,p_fact_adr_city		\n");
		query.append("	    ,p_fact_adr_street		\n");
		query.append("	    ,p_fact_adr_house		\n");
		query.append("	    ,p_fact_adr_case		\n");
		query.append("	    ,p_fact_adr_apartment		\n");
		query.append("	    ,p_phone_home		\n");
		query.append("	    ,p_phone_mobile		\n");
		query.append("	    ,p_email		\n");
		query.append("	    ,p_cd_nat_prs_group		\n");
		query.append("	    ,p_name_other_variant_group		\n");
		query.append("	    ,p_result_msg)}  	\n");*/
		updateQuery=query.toString();
	}
	/** произвести синхронизацию данных объекта и установленных данных в базе данных 
	 * @param connection - соединение с базой данных, на основании которого нужно проводить все манипуляции 
	 * @return 
	 * <li>null - запись произведена успешно </li>
	 * <li>text of Exception or Error </li>
	 * <br>
	 * <b>Важно: Connection.commit присутствует в данном методе (или Rollback в случае неудачного копирования)<b><br>
	 * Соединение должно быть закрыто после всех манипуляций
	 * */
	public String updateObjectInDatabase(Connection connection){
		String returnValue=null;
		try{
			CallableStatement statement=connection.prepareCall(updateQuery);
			statement.setInt(2, this.id);
			statement.setString(3, this.countryKod);
			statement.setString(4, this.postIndex);
			statement.setString(5, this.oblastId);
			statement.setString(6, this.region);
			statement.setString(7, this.city);
			statement.setString(8, this.street);
			statement.setString(9, this.house);
			statement.setString(10, this.housing);
			statement.setString(11, this.flatNumber);
			statement.setString(12, this.phoneHome);
			statement.setString(13, this.phoneMobile);
			statement.setString(14, this.email);
			statement.setString(15, this.profId);
			statement.setString(16, this.profOther);
			statement.registerOutParameter(17, java.sql.Types.VARCHAR);
			statement.registerOutParameter(1,java.sql.Types.INTEGER);

			statement.executeQuery();
			if(statement.getInt(1)!=0){
				throw new Exception(statement.getString(17));
			}
			connection.commit();
		}catch(Exception ex){
			returnValue=ex.getMessage();
			System.out.println("UserParent#updateObjectInDatabase Exception: "+returnValue);
			try{
				connection.rollback();
			}catch(Exception exInner){};
		}
		return returnValue;
	}
	/**
	 * @param sex the sex to set
	 */
	public void setSex(String sex, Connection connection) {
		this.sex = sex;
		/*
		 *  SELECT * --lookup_code, meaning 
			FROM 
			bc_admin.vc_lookup_values 
			--WHERE name_lookup_type = 'MALE_FEMALE' ORDER BY number_value
		 */
	}
	/**
	 * @param postIndex the postIndex to set
	 */
	public void setPostIndex(String postIndex) {
		this.postIndex = postIndex;
	}
	/**
	 * @param oblast the oblast to set
	 * @param connection - текущее соединение с базой данных для получения кода области
	 */
	public void setOblast(String oblast, Connection connection) {
		this.oblast = oblast;
		// выбрать область из справочника
		ResultSet rs=null;
		PreparedStatement preparedStatement=null;
		try{
			preparedStatement=connection.prepareStatement("select id_oblast from bc_admin.vc_oblast_all where name_oblast=?");
			preparedStatement.setString(1, oblast);
			rs=preparedStatement.executeQuery();
			if(rs.next()){
				this.oblastId=rs.getString(1);
			}else{
				this.oblastId=null;
			}
		}catch(Exception ex){
			System.out.println("UserParent#setOblast Exception: "+ex.getMessage());
		}finally{
			try{
				rs.close();
			}catch(Exception ex){};
			try{
				preparedStatement.close();
			}catch(Exception ex){};
		}
		
		/*
select *
from bc_admin.vc_oblast_all
		 * 
		 */
	}
	/**
	 * @param region the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}
	/**
	 * @param house the house to set
	 */
	public void setHouse(String house) {
		this.house = house;
	}
	/**
	 * @param housing the housing to set
	 */
	public void setHousing(String housing) {
		this.housing = housing;
	}
	/**
	 * @param flatNumber the flatNumber to set
	 */
	public void setFlatNumber(String flatNumber) {
		this.flatNumber = flatNumber;
	}
	/**
	 * @param phoneHome the phoneHome to set
	 */
	public void setPhoneHome(String phoneHome) {
		this.phoneHome = phoneHome;
	}
	/**
	 * @param phoneMobile the phoneMobile to set
	 */
	public void setPhoneMobile(String phoneMobile) {
		this.phoneMobile = phoneMobile;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @param prof the prof to set
	 * @param connection - текущее соединение с базой данных для 
	 */
	public void setProf(String prof, Connection connection) {
		this.prof = prof;
		ResultSet rs=null;
		PreparedStatement statement=null;
		try{
			statement=connection.prepareStatement("select * from bc_admin.vc_nat_prs_group_all where name_nat_prs_group=?");
			statement.setString(1, this.prof);
			rs=statement.executeQuery();
			if(rs.next()){
				// найден данный вариант из списка предложенных вариантов 
				this.profId=rs.getString("CD_NAT_PRS_GROUP");
				this.profOther=null;
			}else{
				// вариант не найден  нужно сделать тип "OTHER" и установить значение для другие
				this.profId=UsersParent.keyProfOther;
				this.profOther=prof;
			}
		}catch(Exception ex){
			System.err.println("UsersParent#setProf Exception: "+ex.getMessage());
		}finally{
			try{
				rs.getStatement().close();
			}catch(Exception ex){};
			try{
				statement.close();
			}catch(Exception ex){};
		}
	}
	
	
}
