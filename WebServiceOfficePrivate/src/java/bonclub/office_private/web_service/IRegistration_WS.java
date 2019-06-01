package bonclub.office_private.web_service;

import bonclub.office_private.web_service.common_objects.RegistrationResponse;
import bonclub.office_private.web_service.db_function.DbException;
import bonclub.office_private.web_service.db_function.DbFunction;
import bonclub.office_private.web_service.db_function.wrap.WrapPerson;
import bonclub.office_private.web_service.result.WsReturnValue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import javax.naming.NamingException;

import org.apache.log4j.Logger;


public class IRegistration_WS extends AbstractWebServiceFunction implements IRegistration{

public IRegistration_WS(){  }
	private Logger logger=Logger.getLogger(IRegistration_WS.class);

    public WsReturnValue<RegistrationResponse> getNewClient(String bonCardNumber, 
    										 String password, 
    										 Date birthDay, 
    										 String email, 
    										 String phone) {
        bonCardNumber=decode(bonCardNumber);
        password=decode(password);
        email=decode(email);
        phone=decode(phone);
		// INFO OfficePrivate запрос нового пользователя 
		Connection connection=null;
		try{
			connection=this.getDataSource().getConnection();
			WrapPerson wrap=getDbFunction().checkPerson(connection, bonCardNumber, password, DbFunction.language.RU.toString());
			RegistrationResponse response=new RegistrationResponse();
			wrap.copyTo(response);
			return new WsReturnValue<RegistrationResponse>(response);
		}catch(SQLException ex){
			logger.error("getNewClient SQLException: "+ex.getMessage());
			return new WsReturnValue<RegistrationResponse>(ex);
		}catch(NamingException ex){
			logger.error("getNewClient SQLException: "+ex.getMessage());
			return new WsReturnValue<RegistrationResponse>(ex);
		}catch(DbException ex){
			return new WsReturnValue<RegistrationResponse>(ex);
		}finally{
			safeCloseConnection(connection);
		}
    }

}