package bc.connection.singlepool;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import javax.naming.*;
import javax.sql.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

import BonCard.DataBase.UtilityConnector;
import BonCard.DataBase.UtilityConnector.FunctionExecutionResult;
import bc.connection.*;

public class JndiConnector implements SessionConnector{
	private static String SESSION_ID_MARKER="SESSION_ID";
	private static String CONTAINER_CONTEXT_NAME = "java:comp/env/jdbc/wmpu";
	private final static Logger LOGGER=Logger.getLogger(JndiConnector.class);
	
	private int executionErrorCode;
	private String executionErrorMessage;
	
	public JndiConnector () {
		LOGGER.setLevel(Level.ERROR);
	}
	
	public int getErrorCode() {
		return executionErrorCode;
	}
	
	public String getErrorMessage() {
		return executionErrorMessage;
	}

	private Connection getConnectionFromJndi() {
		try {
			Context initContext = new InitialContext();
			DataSource ds = (DataSource) initContext
					.lookup(CONTAINER_CONTEXT_NAME);
			return ds.getConnection();
		} catch (NamingException ex) {
			LOGGER.error("!!! critial ERROR - can't retrieve connection !!!"+ ex);
			return null;
		} catch (SQLException ex) {
			LOGGER.error("!!! critial ERROR - can't retrieve connection !!!", ex);
			return null;
		} catch (Exception ex) {
			LOGGER.error("!!! critial ERROR - can't retrieve connection !!!", ex);
			return null;
		}
	}

	@Override
	public void closeConnection(Connection connection) {
		UtilityConnector.closeQuietly(connection);
	}

	@Override
	public Connection getConnection(String moduleName, String userName, String password, String sessionId, String language, String lastConnectionIp, String SMSConfirmCode) {
		Connection returnValue=getConnectionFromJndi();
		if(executeFunction(returnValue,"fnc_check_connection", new String[]{moduleName, userName, password, sessionId, language, lastConnectionIp, SMSConfirmCode})){
			MDC.put(SESSION_ID_MARKER, sessionId);
			return returnValue;
		}else{
			MDC.put(SESSION_ID_MARKER, StringUtils.EMPTY);
			this.closeConnection(returnValue);
			return null;
		}
	}

	@Override
	public Connection getConnection(String sessionId) {
		Connection returnValue=getConnectionFromJndi();
		if(executeFunction(returnValue,"FNC_CHECK_SESSION", new String[]{sessionId})){
			MDC.put(SESSION_ID_MARKER, sessionId);
			return returnValue;
		}else{
			MDC.put(SESSION_ID_MARKER, StringUtils.EMPTY);
			this.closeConnection(returnValue);
			return null;
		}
	}

	@Override
	public void removeSessionId(String sessionId) {
		Connection returnValue=getConnectionFromJndi();
		if(returnValue==null){
			throw new IllegalAccessError("check JNDI paraemters, can't retrieve connection from Context");
		}
		try{
			executeFunction(returnValue,"FNC_CLOSE_SESSION", new String[]{sessionId});
		}finally{
			MDC.put(SESSION_ID_MARKER, StringUtils.EMPTY);
			this.closeConnection(returnValue);
		}
	}

	@Override
	public void removeSessionsByUser(String userId) {
		Connection returnValue=getConnectionFromJndi();
		try{
			executeFunction(returnValue,"FNC_CLOSE_USER_SESSIONS", new String[]{userId});
		}finally{
			this.closeConnection(returnValue);
		}
	}
	

	private boolean executeFunction(Connection connection, String functionName, Object[] parameters) {
		this.clearState();
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
			setState(result);
			return result.isPositive();
		}catch(SQLException ex){
			LOGGER.error("function execution: "+functionName+"  Exception:"+ex.getMessage());
			setState(UtilityConnector.FunctionExecutionResult.errorInstance(ex.getMessage()));
			return false;
		}
	}

	
	private void setState(FunctionExecutionResult result) {
		executionErrorCode=result.getErrorCode();
		executionErrorMessage=result.getErrorMessage();
	}

	private void clearState() {
		this.executionErrorCode=0;
		this.executionErrorMessage=null;
	}
	
	/*public Map<String, String> getConnectionMap(){
		Map<String, String> cn = new HashMap<String, String>();
		try {
			Context initContext = new InitialContext();
			DataSource ds = (DataSource) initContext
					.lookup(CONTAINER_CONTEXT_NAME);
			for (i=0;i<ds.)
			return ds.getConnection();
		} catch (NamingException ex) {
			LOGGER.error("!!! critial ERROR - can't retrieve connection !!!"+ ex);
			return null;
		} catch (SQLException ex) {
			LOGGER.error("!!! critial ERROR - can't retrieve connection !!!", ex);
			return null;
		} catch (Exception ex) {
			LOGGER.error("!!! critial ERROR - can't retrieve connection !!!", ex);
			return null;
		}
	}*/

}
