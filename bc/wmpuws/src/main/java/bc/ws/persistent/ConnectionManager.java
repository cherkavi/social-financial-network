package bc.ws.persistent;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Random;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bc.ws.service.IpDetector;

@Service
public class ConnectionManager {
	private final static Logger LOGGER=Logger.getLogger(ConnectionManager.class);

	private final static String MODULE_NAME="WEBSERVICE";
	private final static String DEFAULT_LANG="RU";
	
	private final static ThreadLocal<String> CURRENT_SESSION=new ThreadLocal<String>();
	
	@Autowired
	private DataSource dataSource;
	
	public Connection open(String userName, String password) throws SQLException{
		Connection returnValue=dataSource.getConnection();
		String currentSessionId=generateRandomSessionId();
		CURRENT_SESSION.set(currentSessionId);
		if(executeFunction(returnValue,
							"fnc_check_connection", 
							new String[]{MODULE_NAME, userName, password, currentSessionId, DEFAULT_LANG, null, null})
							){
			// MDC.put(SESSION_ID_MARKER, sessionId);
			return returnValue;
		}else{
			// MDC.put(SESSION_ID_MARKER, StringUtils.EMPTY);
			UtilityConnector.closeQuietly(returnValue);
			LOGGER.warn("can't init connection to DB with credentials:"+userName+"/"+password);
			return null;
		}
	}
	
	/**
	 * open connection using hash signature instead of password
	 * @param userName
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public Connection openHash(String userName, String date, String signature) throws SQLException{
		checkDate(date);
		String currentSessionId=generateRandomSessionId();
		CURRENT_SESSION.set(currentSessionId);
		Connection returnValue=dataSource.getConnection();
		LOGGER.debug("remote IP address:"+IpDetector.PARAMETER.get());
		if(executeFunction(returnValue,
							"fnc_check_connection_hash", 
							new String[]{MODULE_NAME, userName, date, signature, currentSessionId, DEFAULT_LANG, IpDetector.PARAMETER.get(), null})
							){
			// MDC.put(SESSION_ID_MARKER, sessionId);
			return returnValue;
		}else{
			// MDC.put(SESSION_ID_MARKER, StringUtils.EMPTY);
			UtilityConnector.closeQuietly(returnValue);
			LOGGER.warn("can't init connection to DB with credentials:"+userName+"/"+signature);
			return null;
		}
	}
	
	private final static String DATETIME_PATTERN= "yyyy-MM-dd'T'HH:mm:ss";
	
	/**
	 * check format of the input date: 2016-10-22T22:25:28+03:00
	 * @param date
	 * @throws {@link SQLException} otherwise
	 */
	static void checkDate(String date) throws SQLException{
		SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_PATTERN);
		try {
			sdf.parse(date);
		} catch (ParseException e) {
			throw new SQLException("can't parse the date: "+date);
		}
	}

	private static String generateRandomSessionId(){		
		return Integer.toString(new Random().nextInt(Integer.MAX_VALUE))+Long.toString(System.currentTimeMillis());
	}
	
	public void close(Connection connection) {
		if(connection==null){
			return;
		}
		try{
			executeFunction(connection,"FNC_CLOSE_SESSION", 
					new String[]{CURRENT_SESSION.get()});
		}finally{
			// MDC.put(SESSION_ID_MARKER, StringUtils.EMPTY);
			UtilityConnector.closeQuietly(connection);
		}
	}
	
	
	private boolean executeFunction(Connection connection, String functionName, Object[] parameters) {
		StringBuilder logMessage = new StringBuilder();
		try{
			logMessage.append("execute "+functionName+"(");
			int parametersSize=(parameters==null?0:parameters.length);
			for(int index=0;index<parametersSize; index++){
				if (index==0) {
					logMessage.append("'" + parameters[index] + "'");
				} else {
					logMessage.append(",'" + parameters[index] + "'");
				}
			}
			logMessage.append(")");
			
			//LOGGER.error("function execution: "+functionName);
			UtilityConnector.FunctionExecutionResult result = UtilityConnector.executeFunctionByName(connection, functionName, parameters);
			logMessage.append(", errorCode="+result.getErrorCode());
			if (!(result.getErrorMessage() == null || "".equalsIgnoreCase(result.getErrorMessage()))) {
				logMessage.append(", errorMessage="+result.getErrorMessage());
				LOGGER.error(logMessage.toString());
			} else {
				logMessage.append(", no message");
			}
			return result.isPositive();
		}catch(SQLException ex){
			LOGGER.error("function execution: "+functionName+"  Exception:"+ex.getMessage());
			return false;
		}
	}
	
}
