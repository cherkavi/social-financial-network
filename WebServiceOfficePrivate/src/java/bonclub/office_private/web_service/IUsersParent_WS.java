package bonclub.office_private.web_service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.naming.NamingException;

import org.apache.log4j.Logger;

import bonclub.office_private.web_service.common_objects.Purse;
import bonclub.office_private.web_service.common_objects.UsersParent;
import bonclub.office_private.web_service.db_function.DbException;
import bonclub.office_private.web_service.db_function.DbFunction;
import bonclub.office_private.web_service.db_function.SharedFunction;
import bonclub.office_private.web_service.db_function.wrap.WrapPerson;
import bonclub.office_private.web_service.result.WsReturnValue;


public class IUsersParent_WS extends AbstractWebServiceFunction implements IUsersParent{

public IUsersParent_WS(){  }
	private static Logger logger=Logger.getLogger(IUsersParent_WS.class);

	
	public WsReturnValue<UsersParent> getUserParentByBoncardNumber(String boncardNumber, String password){
        boncardNumber=decode(boncardNumber);
        password=decode(password);
        
		boncardNumber=SharedFunction.getShortBoncardNumber(boncardNumber);
		WsReturnValue<UsersParent> returnValue=null;
		Connection connection=null;
		try{
			connection=getConnection();
			WrapPerson wrapPerson=getDbFunction().checkPerson(connection, boncardNumber, password, DbFunction.language.RU.toString());
			UsersParent usersParent=new UsersParent();
			wrapPerson.copyTo(usersParent);
			returnValue=new WsReturnValue<UsersParent>(usersParent);
		}catch(SQLException ex){
			returnValue=new WsReturnValue<UsersParent>(WsReturnValue.InternalError.SQL, ex.getMessage());
			logger.error("getNewClient SQLException: "+ex.getMessage());
		}catch(NamingException ex){
			returnValue=new WsReturnValue<UsersParent>(WsReturnValue.InternalError.Service, ex.getMessage());
			logger.error("getNewClient NamingException: "+ex.getMessage());
		}catch(DbException ex){
			returnValue=new WsReturnValue<UsersParent>(ex);
			logger.debug("getUserParentByBoncardNumber DbException: "+ex.getMessage());
		}finally{
			this.safeCloseConnection(connection);
		}
		return returnValue;
    }

	public WsReturnValue<Integer> isUserParentByEmail(String email){
        email=decode(email);
        WsReturnValue<Integer> returnValue=null;
		Connection connection=null;
		try{
			connection=getConnection();
			returnValue=new WsReturnValue<Integer>(getDbFunction().personParametersByEmail(connection, email, DbFunction.language.RU.toString()));
		}catch(DbException ex){
			returnValue=new WsReturnValue<Integer>(ex);
		}catch(SQLException ex){
			logger.error("isUserParentByEmail Exception: "+ex.getMessage());
			returnValue=new WsReturnValue<Integer>(ex);
		}catch(NamingException ex){
			logger.error("isUserParentByEmail Exception: "+ex.getMessage());
			returnValue=new WsReturnValue<Integer>(ex);
		}finally{
			this.safeCloseConnection(connection);
		}
		return returnValue;
    }

    public WsReturnValue<Boolean> updateObjectInDatabase(    	
    		/** уникальный идентификатор пользователя */
            Integer id,
        	/** пароль для доступа к ресурсу */
            String password,
        	/** код страны */
            String countryKod,
        	/** информация о пользователе */
            String fullName,
        	/** Номер бон-карты */
            String boncardNumber,
        	/** Клуб */
            String club,    
        	/** состояние карты */
            String cardState,
        	/** Категория бон-карты (боны)*/
            String bonCategory,     
        	/** Категория бон-карты (скидки)*/
            String discontCategory, 
        	/** Доступно бонов на карте */
            String bonAviable,      
        	/** Накоплено бонов на карте */
            String bonStorage,      
        	/** Время пользования картой */
            Calendar timeUse,       
        	/** Имя: */
            String name,
        	/** Фамилия: */
            String surname,
        	/** Отчество: */
            String fatherName,
        	/** Дата рождения: */
            String birthDay,
        	/** Дата рождения в виде java.util.Date */
            Date birthDate,
        	/** Пол: */
            String sex,
        	/** индекс */
            String postIndex, 
        	/**  область */
            String oblast, 
        	/** код области */
            String oblastId,
        	/**  район */
            String region,
        	/**  город */
            String city, 
        	/**  улица */
            String street, 
        	/**  дом */
            String house, 
        	/**  корпус */
            String housing, 
        	/**  номер квартиры */
            String flatNumber, 
        	/** домашний телефон */
            String phoneHome,  
        	/** мобильный телефон */
            String phoneMobile, 
        	/** e-mail */
            String email, 
        	/** профессия - текстовое представление  */
            String prof, 
        	/** профессия текстовый акроним из базы данных */
            String profId,
        	/** другой вариант профессии - не из списка */
            String profOther,
        	/** дата регистрации данного объекта */
            Date registrationDate,
        	
        	/** Накоплено бонов за период (копейки) */
            String balBonPer,
        	/** Накоплено скидки (копейки) */
            String balDiscPer,
        	/** Следующая дата перевода бонов в доступные */
            Date nextDateMov,
        	/** Следующая дата перевода категорий */
            Date nextDateCalc, 
        	
        	/** серийный номер карты */
            String cardSerialNumber,
        	/** код эмитента карты */
            Integer idEmitentCard,
        	/** наименование эмитента карты */
            String nameEmitentCard,
        	/** код платежной системы */
            Integer idPaymentSystemCard,
        	/** наименование платежной системы */
            String namePaymentSystemCard,
        	/** текст ошибки, который был выявлен при наполнении/получении объекта  
        	 * */
            String errorMessage        
) {
		WsReturnValue<Boolean> returnValue=null;
		Connection connection=null;
		try{
			connection=this.getConnection();
			java.sql.Date dateOfBirth=null;
			if(birthDate!=null){
				dateOfBirth=new java.sql.Date(birthDate.getTime());
			};
			getDbFunction().updatePerson(connection, 
//					 int userId,//2
								 id, 
//								 String bonCardNumberShort, // 3
								 SharedFunction.getShortBoncardNumber(boncardNumber),
//								 String password,// 4
								 password, 
//								 String surname,//5
								 surname, 
//							     String name,//6
								 name, 
//							     String fatherName,//7
								 fatherName, 
//							     Date dateOfBirth,//8
								 dateOfBirth, 
//							     String sex,//9
								 sex, 
//							     String codeCountry,//10
								 countryKod, 
//							     String zipCode, //11
								 postIndex, 
//							     String idOblast,//12
								 oblastId, 
//							     String adrDistrict,//13
								 region, 
//							     String adrCity,//14
								 city, 
//							     String adrStreet,//15
								 street, 
//							     String adrHouse,//16
								 house, 
//							     String adrCase,//17
								 housing, 
//							     String adrApartment,//18
								 flatNumber, 
//							     String phoneHome,//19
								 phoneHome, 
//							     String phoneMobile,//20
								 phoneMobile, 
//							     String email,//21
								 email, 
//							     String personGroup,//22
								 profId, 
//							     String otherVariantGroup,//23
								 profOther, 
//							     String language//24
								 DbFunction.language.RU.toString());
			returnValue=new WsReturnValue<Boolean>(Boolean.TRUE);
		}catch(SQLException ex){
			logger.error("updateObjectInDatabase Exception: "+ex.getMessage());
			returnValue=new WsReturnValue<Boolean>(ex);
		}catch(DbException ex){
			returnValue=new WsReturnValue<Boolean>(ex);
		}catch(NamingException ex){
			returnValue=new WsReturnValue<Boolean>(ex);
		}finally{
			safeCloseConnection(connection);
		}
		return returnValue;
    }

    
	public WsReturnValue<List<Purse>> getPurseOfBonCard(String card_serial_number, 
    									 Integer id_issuer, 
    									 Integer id_payment_system){
		Connection connection=null;
		try{
			connection=this.getConnection();
			return new WsReturnValue<List<Purse>>(getDbFunction().getPurseOfBonCard(connection, decode(card_serial_number), id_issuer, id_payment_system));
		}catch(SQLException ex){
			logger.error("getPurseOfBonCard Exception: "+ex.getMessage());
			return new WsReturnValue<List<Purse>>(ex);
		}catch(NamingException ex){
			logger.error("getPurseOfBonCard Exception: "+ex.getMessage());
			return new WsReturnValue<List<Purse>>(ex);
		}catch(IllegalAccessException ex){
			logger.error("getPurseOfBonCard Exception: "+ex.getMessage());
			return new WsReturnValue<List<Purse>>(ex);
		}catch(InstantiationException ex){
			logger.error("getPurseOfBonCard Exception: "+ex.getMessage());
			return new WsReturnValue<List<Purse>>(ex);
		}
		finally{
			safeCloseConnection(connection);
		}
    }
    
}