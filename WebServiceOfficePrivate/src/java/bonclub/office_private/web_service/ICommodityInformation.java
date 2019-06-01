package bonclub.office_private.web_service;

import bonclub.office_private.web_service.common_objects.CommodityInformation;
import bonclub.office_private.web_service.result.WsReturnValue;

import javax.jws.WebMethod; 
import javax.jws.WebParam; 
import javax.jws.WebService; 

@WebService
public interface ICommodityInformation{
	/**
	 * 1. Получение информации по коду бон-карты
	 * @param card_serial_number
	 * @param id_issuer
	 * @param id_payment_system
	 * @return
	 */
    @WebMethod
    public WsReturnValue<CommodityInformation[]> getInformationByBonCard(
        @WebParam(name="card_serial_number") String card_serial_number, 
        @WebParam(name="id_issuer") Integer id_issuer, 
        @WebParam(name="id_payment_system") Integer id_payment_system);

    /**
     * 2. Получение информации по ИД транзакции
     * @param id_trans
     * @return
     */
    @WebMethod
    public WsReturnValue<CommodityInformation[]> getInformationByIdTransaction(
        @WebParam(name="id_trans") Integer id_trans);

}