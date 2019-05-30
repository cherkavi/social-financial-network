package bc.ws.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import bc.ws.domain.TerminalMenuResult;
import bc.ws.exception.PersistentException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import bc.ws.domain.EnvironmentSettings;
import bc.ws.exception.WsException;
import bc.ws.persistent.ConnectionManager;
import bc.ws.persistent.UserParams;

@WebService
public class UserParamsService {
	private final static Logger LOGGER=Logger.getLogger(UserParamsService.class);
			
	@Autowired
	ConnectionManager connectionManager;
	
	@WebMethod
	public EnvironmentSettings getSettings(
			@WebParam(name="userName")
			String userName,
			@WebParam(name="operationDate")
			String operationDate, 
			@WebParam(name="signature")
			String signature
			) throws WsException{
		LOGGER.debug("get settings");
		Connection connection=null;
		try{
			connection=connectionManager.openHash(userName, operationDate, signature);
			UserParams operation=new UserParams(connection);
			return operation.getSettings();
		}catch(SQLException ex){
			LOGGER.warn(userName+"/"+signature, ex);
			throw new WsException("check parameters");
		}catch(PersistentException ex){
            LOGGER.warn(userName+"/"+signature, ex);
            throw new WsException("check parameters");
        } finally{
			connectionManager.close(connection);
		}
	}
	
	@WebMethod
	public TerminalMenuResult[] getTerminalMenu(
			@WebParam(name="userName")
			String userName, 
			@WebParam(name="operationDate")
			String operationDate, 
			@WebParam(name="signature")
			String signature,
            @WebParam(name="terminalId")
            String terminalId
			) throws WsException{
		LOGGER.debug("get operations");
		Connection connection=null;
		try{
			connection=connectionManager.openHash(userName, operationDate, signature);
			UserParams operation=new UserParams(connection);
			List<TerminalMenuResult> returnValue=operation.getOperations(terminalId);
			return returnValue.toArray(new TerminalMenuResult[returnValue.size()]);
		}catch(SQLException ex){
			LOGGER.warn(userName+"/"+signature, ex);
			throw new WsException("check parameters");
		}finally{
			connectionManager.close(connection);
		}
	}
	
}
