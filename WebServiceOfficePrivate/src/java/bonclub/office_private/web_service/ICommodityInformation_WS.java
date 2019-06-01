package bonclub.office_private.web_service;

import bonclub.office_private.web_service.common_objects.CommodityInformation;
import bonclub.office_private.web_service.result.WsReturnValue;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;

import org.apache.log4j.Logger;


public class ICommodityInformation_WS extends AbstractWebServiceFunction implements ICommodityInformation{

public ICommodityInformation_WS(){  }
	private final static Logger logger=Logger.getLogger(ICommodityInformation_WS.class);
	

	public WsReturnValue<CommodityInformation[]> getInformationByBonCard(String card_serial_number, Integer id_issuer, Integer id_payment_system) {
		Connection connection=null;
		try{
			return new WsReturnValue<CommodityInformation[]>(this.getDbFunction().getInformationByBonCard(connection=getConnection(), decode(card_serial_number), id_issuer, id_payment_system));
		}catch(SQLException ex){
			logger.error("getInformationByBonCard Exception: "+ex.getMessage());
			return new WsReturnValue<CommodityInformation[]>(ex);
		}catch(NamingException ex){
			logger.error("getInformationByBonCard Exception: "+ex.getMessage());
			return new WsReturnValue<CommodityInformation[]>(ex);
		}catch(InstantiationException ex){
			logger.error("getInformationByBonCard Exception: "+ex.getMessage());
			return new WsReturnValue<CommodityInformation[]>(ex);
		}catch(IllegalAccessException ex){
			logger.error("getInformationByBonCard Exception: "+ex.getMessage());
			return new WsReturnValue<CommodityInformation[]>(ex);
		}
		finally{
			safeCloseConnection(connection);
		}
    }

	public WsReturnValue<CommodityInformation[]> getInformationByIdTransaction(Integer id_trans){
		Connection connection=null;
		try{
			return new WsReturnValue<CommodityInformation[]>(this.getDbFunction().getInformationByIdTransaction(connection=getConnection(), id_trans));
		}catch(SQLException ex){
			logger.error("getInformationByBonCard Exception: "+ex.getMessage());
			return new WsReturnValue<CommodityInformation[]>(ex);
		}catch(NamingException ex){
			logger.error("getInformationByBonCard Exception: "+ex.getMessage());
			return new WsReturnValue<CommodityInformation[]>(ex);
		}catch(InstantiationException ex){
			logger.error("getInformationByBonCard Exception: "+ex.getMessage());
			return new WsReturnValue<CommodityInformation[]>(ex);
		}catch(IllegalAccessException ex){
			logger.error("getInformationByBonCard Exception: "+ex.getMessage());
			return new WsReturnValue<CommodityInformation[]>(ex);
		}
		finally{
			safeCloseConnection(connection);
		}
   }

}