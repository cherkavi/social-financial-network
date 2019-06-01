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
    	/** ���������� ������������� ������������ */
        @WebParam(name="id") Integer id,
    	/** ������ ��� ������� � ������� */
        @WebParam(name="password") String password,
    	/** ��� ������ */
        @WebParam(name="countryKod") String countryKod,
    	/** ���������� � ������������ */
        @WebParam(name="fullName") String fullName,
    	/** ����� ���-����� */
        @WebParam(name="boncardNumber") String boncardNumber,
    	/** ���� */
        @WebParam(name="club") String club,    
    	/** ��������� ����� */
        @WebParam(name="cardState") String cardState,
    	/** ��������� ���-����� (����)*/
        @WebParam(name="bonCategory") String bonCategory,     
    	/** ��������� ���-����� (������)*/
        @WebParam(name="discontCategory") String discontCategory, 
    	/** �������� ����� �� ����� */
        @WebParam(name="bonAviable") String bonAviable,      
    	/** ��������� ����� �� ����� */
        @WebParam(name="bonStorage") String bonStorage,      
    	/** ����� ����������� ������ */
        @WebParam(name="timeUse") Calendar timeUse,       
    	/** ���: */
        @WebParam(name="name") String name,
    	/** �������: */
        @WebParam(name="surname") String surname,
    	/** ��������: */
        @WebParam(name="fatherName") String fatherName,
    	/** ���� ��������: */
        @WebParam(name="birthDay") String birthDay,
    	/** ���� �������� � ���� java.util.Date */
        @WebParam(name="birthDate") Date birthDate,
    	/** ���: */
        @WebParam(name="sex") String sex,
    	/** ������ */
        @WebParam(name="postIndex") String postIndex, 
    	/**  ������� */
        @WebParam(name="oblast") String oblast, 
    	/** ��� ������� */
        @WebParam(name="oblastId") String oblastId,
    	/**  ����� */
        @WebParam(name="region") String region, 
    	/**  ����� */
        @WebParam(name="city") String city, 
    	/**  ����� */
        @WebParam(name="street") String street,
    	/**  ��� */
        @WebParam(name="house") String house, 
    	/**  ������ */
        @WebParam(name="housing") String housing, 
    	/**  ����� �������� */
        @WebParam(name="flatNumber") String flatNumber, 
    	/** �������� ������� */
        @WebParam(name="phoneHome") String phoneHome,  
    	/** ��������� ������� */
        @WebParam(name="phoneMobile") String phoneMobile, 
    	/** e-mail */
        @WebParam(name="email") String email, 
    	/** ��������� - ��������� �������������  */
        @WebParam(name="prof") String prof, 
    	/** ��������� ��������� ������� �� ���� ������ */
        @WebParam(name="profId") String profId,
    	/** ������ ������� ��������� - �� �� ������ */
        @WebParam(name="profOther") String profOther,
    	/** ���� ����������� ������� ������� */
        @WebParam(name="registrationDate") Date registrationDate,
    	
    	/** ��������� ����� �� ������ (�������) */
        @WebParam(name="balBonPer") String balBonPer,
    	/** ��������� ������ (�������) */
        @WebParam(name="balDiscPer") String balDiscPer,
    	/** ��������� ���� �������� ����� � ��������� */
        @WebParam(name="nextDateMov") Date nextDateMov,
    	/** ��������� ���� �������� ��������� */
        @WebParam(name="nextDateCalc") Date nextDateCalc, 
    	
    	/** �������� ����� ����� */
        @WebParam(name="cardSerialNumber") String cardSerialNumber,
    	/** ��� �������� ����� */
        @WebParam(name="idEmitentCard") Integer idEmitentCard,
    	/** ������������ �������� ����� */
        @WebParam(name="nameEmitentCard") String nameEmitentCard,
    	/** ��� ��������� ������� */
        @WebParam(name="idPaymentSystemCard") Integer idPaymentSystemCard,
    	/** ������������ ��������� ������� */
        @WebParam(name="namePaymentSystemCard") String namePaymentSystemCard,
    	/** ����� ������, ������� ��� ������� ��� ����������/��������� �������  */
        @WebParam(name="errorMessage") String errorMessage        
    	);

    @WebMethod
    public WsReturnValue<List<Purse>> getPurseOfBonCard(
        @WebParam(name="card_serial_number") String card_serial_number, 
        @WebParam(name="id_issuer") Integer id_issuer, 
        @WebParam(name="id_payment_system") Integer id_payment_system);
}