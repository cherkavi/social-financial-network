package bonclub.office_private.web_service;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;

import org.apache.log4j.Logger;

import bonclub.office_private.web_service.db_function.DbException;
import bonclub.office_private.web_service.db_function.DbFunction;
import bonclub.office_private.web_service.result.WsReturnValue;


public class IEventNotifier_WS extends AbstractWebServiceFunction implements IEventNotifier{

	public IEventNotifier_WS() {  
		
	}
	
	private static Logger logger=Logger.getLogger(IEventNotifier_WS.class);
	
	public WsReturnValue<Boolean> notifyServerAboutSendPasswordBySms(String phoneNumber){
		Connection connection=null;
		try{
			this.getDbFunction().sendPasswordByPhone(connection=this.getConnection(), decode(phoneNumber), DbFunction.language.RU.toString());
			return new WsReturnValue<Boolean>(Boolean.TRUE);
		}catch(NamingException ex ){
			logger.error("notifyServerAboutSendPasswordBySms Exception: "+ex.getMessage());
			return new WsReturnValue<Boolean>(ex);
		}catch(SQLException ex){
			logger.error("notifyServerAboutSendPasswordBySms Exception: "+ex.getMessage());
			return new WsReturnValue<Boolean>(ex);
		}catch(DbException ex){
			return new WsReturnValue<Boolean>(ex);
		}
		finally{
			this.safeCloseConnection(connection);
		}
    }

    public WsReturnValue<Boolean> sendLoginAndPasswordByMail(String boncardNumber){
		// оповестить сервер о необходимости посылки данных ( логина и пароля) на EMail по уникальному идентификатору пользователя
		Connection connection=null;
		try{
			getDbFunction().sendLoginToEmail(connection=this.getConnection(), 
										  	 decode(boncardNumber), 
										  	 DbFunction.language.RU.toString());
			return new WsReturnValue<Boolean>(Boolean.TRUE);
		}catch(SQLException ex){
			logger.error("sendLoginAndPasswordByMail Exception: "+ex.getMessage());
			return new WsReturnValue<Boolean>(ex);
		}catch(NamingException ex){
			logger.error("sendLoginAndPasswordByMail Exception: "+ex.getMessage());
			return new WsReturnValue<Boolean>(ex);
		}catch(DbException ex){
			return new WsReturnValue<Boolean>(ex);
		}
		finally{
			safeCloseConnection(connection);
		}
    }

    public WsReturnValue<Boolean> sendLoginAndPasswordByMailByEmail(String email){
		// оповестить сервер о необходимости посылки данных ( логина и пароля) на EMail по уникальному идентификатору пользователя
		Connection connection=null;
		try{
			getDbFunction().sendLoginToEmailByMail(connection=this.getConnection(), decode(email), DbFunction.language.RU.toString());
			return new WsReturnValue<Boolean>(Boolean.TRUE);
		}catch(SQLException ex){
			logger.error("sendLoginAndPasswordByMailByEMail Exception: "+ex.getMessage());
			return new WsReturnValue<Boolean>(ex);
		}catch(NamingException ex){
			logger.error("sendLoginAndPasswordByMailByEMail Exception: "+ex.getMessage());
			return new WsReturnValue<Boolean>(ex);
		}catch(DbException ex){
			return new WsReturnValue<Boolean>(ex);
		}
		finally{
			safeCloseConnection(connection);
		}
    }

}