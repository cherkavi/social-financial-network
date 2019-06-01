package bonclub.office_private.web_service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import bonclub.office_private.web_service.common_objects.Purse;
import bonclub.office_private.web_service.common_objects.UsersParent;
import bonclub.office_private.web_service.result.WsReturnValue;

import javax.jws.WebMethod; 
import javax.jws.WebParam; 
import javax.jws.WebService; 

@WebService
public interface IUsersParent{
    @WebMethod
    public WsReturnValue<UsersParent> getUserParentByBoncardNumber(
        @WebParam(name="boncardNumber") String boncardNumber, 
        @WebParam(name="password") String password);
    @WebMethod
    public WsReturnValue<Integer> isUserParentByEmail(
        @WebParam(name="email") String email);
    @WebMethod
    public WsReturnValue<Boolean> updateObjectInDatabase(
    	/** уникальный идентификатор пользователя */
        @WebParam(name="id") Integer id,
    	/** пароль для доступа к ресурсу */
        @WebParam(name="password") String password,
    	/** код страны */
        @WebParam(name="countryKod") String countryKod,
    	/** информация о пользователе */
        @WebParam(name="fullName") String fullName,
    	/** Номер бон-карты */
        @WebParam(name="boncardNumber") String boncardNumber,
    	/** Клуб */
        @WebParam(name="club") String club,    
    	/** состояние карты */
        @WebParam(name="cardState") String cardState,
    	/** Категория бон-карты (боны)*/
        @WebParam(name="bonCategory") String bonCategory,     
    	/** Категория бон-карты (скидки)*/
        @WebParam(name="discontCategory") String discontCategory, 
    	/** Доступно бонов на карте */
        @WebParam(name="bonAviable") String bonAviable,      
    	/** Накоплено бонов на карте */
        @WebParam(name="bonStorage") String bonStorage,      
    	/** Время пользования картой */
        @WebParam(name="timeUse") Calendar timeUse,       
    	/** Имя: */
        @WebParam(name="name") String name,
    	/** Фамилия: */
        @WebParam(name="surname") String surname,
    	/** Отчество: */
        @WebParam(name="fatherName") String fatherName,
    	/** Дата рождения: */
        @WebParam(name="birthDay") String birthDay,
    	/** Дата рождения в виде java.util.Date */
        @WebParam(name="birthDate") Date birthDate,
    	/** Пол: */
        @WebParam(name="sex") String sex,
    	/** индекс */
        @WebParam(name="postIndex") String postIndex, 
    	/**  область */
        @WebParam(name="oblast") String oblast, 
    	/** код области */
        @WebParam(name="oblastId") String oblastId,
    	/**  район */
        @WebParam(name="region") String region, 
    	/**  город */
        @WebParam(name="city") String city, 
    	/**  улица */
        @WebParam(name="street") String street,
    	/**  дом */
        @WebParam(name="house") String house, 
    	/**  корпус */
        @WebParam(name="housing") String housing, 
    	/**  номер квартиры */
        @WebParam(name="flatNumber") String flatNumber, 
    	/** домашний телефон */
        @WebParam(name="phoneHome") String phoneHome,  
    	/** мобильный телефон */
        @WebParam(name="phoneMobile") String phoneMobile, 
    	/** e-mail */
        @WebParam(name="email") String email, 
    	/** профессия - текстовое представление  */
        @WebParam(name="prof") String prof, 
    	/** профессия текстовый акроним из базы данных */
        @WebParam(name="profId") String profId,
    	/** другой вариант профессии - не из списка */
        @WebParam(name="profOther") String profOther,
    	/** дата регистрации данного объекта */
        @WebParam(name="registrationDate") Date registrationDate,
    	
    	/** Накоплено бонов за период (копейки) */
        @WebParam(name="balBonPer") String balBonPer,
    	/** Накоплено скидки (копейки) */
        @WebParam(name="balDiscPer") String balDiscPer,
    	/** Следующая дата перевода бонов в доступные */
        @WebParam(name="nextDateMov") Date nextDateMov,
    	/** Следующая дата перевода категорий */
        @WebParam(name="nextDateCalc") Date nextDateCalc, 
    	
    	/** серийный номер карты */
        @WebParam(name="cardSerialNumber") String cardSerialNumber,
    	/** код эмитента карты */
        @WebParam(name="idEmitentCard") Integer idEmitentCard,
    	/** наименование эмитента карты */
        @WebParam(name="nameEmitentCard") String nameEmitentCard,
    	/** код платежной системы */
        @WebParam(name="idPaymentSystemCard") Integer idPaymentSystemCard,
    	/** наименование платежной системы */
        @WebParam(name="namePaymentSystemCard") String namePaymentSystemCard,
    	/** текст ошибки, который был выявлен при наполнении/получении объекта  */
        @WebParam(name="errorMessage") String errorMessage        
    	);

    @WebMethod
    public WsReturnValue<List<Purse>> getPurseOfBonCard(
        @WebParam(name="card_serial_number") String card_serial_number, 
        @WebParam(name="id_issuer") Integer id_issuer, 
        @WebParam(name="id_payment_system") Integer id_payment_system);
}