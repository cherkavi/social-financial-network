package bc.connection;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import BonCard.DataBase.UtilityConnector;
import bc.connection.singlepool.JndiConnector;


public class Connector {
	public static final ThreadLocal<Integer> errorCode=new ThreadLocal<Integer>();
	public static final ThreadLocal<String> errorMessage=new ThreadLocal<String>();
	
	private static final String CONTEXT_PARAM_SYSTEM_LOGIN="systemUser/login";
	private static final String CONTEXT_PARAM_SYSTEM_PASSWORD="systemUser/password";
	
	private final static String SCHEMA_QUERY="SELECT fnc_get_db_scheme FROM dual";
	private final static String SCHEMA_QUERY_SESSION="11111111111111111111111111111111";
	private final static String SCHEMA_QUERY_LANG="RU";

	private static final Logger LOGGER=Logger.getLogger(Connector.class);
	private static String SYSTEM_LOGIN="";
	private static String SYSTEM_PASSWORD="";
	
	/** default prefix for the tables */
	public static String schemePrefix;
	
	private static void setLogLevel() {
		LOGGER.setLevel(Level.ERROR);
	}

	// private static SessionConnector poolConnector=new SessionPoolConnector();
	private static SessionConnector poolConnector=new JndiConnector();
	
	
	static{
		try {
			Context environmentContext = (Context) new InitialContext().lookup("java:/comp/env");
			SYSTEM_LOGIN=(String)environmentContext.lookup(CONTEXT_PARAM_SYSTEM_LOGIN);
			SYSTEM_PASSWORD=(String)environmentContext.lookup(CONTEXT_PARAM_SYSTEM_PASSWORD);
		} catch (NamingException e) {
			LOGGER.error("critical ERROR - can't read admin parameters ", e);
		}
		
		Connection connection=getAdminConnection(SCHEMA_QUERY_LANG);
		loadSchemePrefix(connection);
		removeSessionId("1");
	}
	
	private static void loadSchemePrefix(Connection connection) {
		Statement statement=null;
		try{
			statement=connection.createStatement();
			ResultSet rs=statement.executeQuery(SCHEMA_QUERY);
			if(rs.next()==false){
				throw new IllegalArgumentException("check access query: "+SCHEMA_QUERY);
			};
			schemePrefix=rs.getString(1)+".";
		}catch(Exception ex){
			System.err.println("loadSchemePrefix Exception:"+ex.getMessage());
		}finally{
			try{
				statement.close();
			}catch(Exception ex){};
		}
		
		
	}
	
	
	/** get Connection from POOL by existing HTTP.SESSION.ID 
	 * @param sessionId - HTTP.SESSION.ID which connected before {@link #getConnection(String, String, String, String, String, String)}
	 * */
	public static synchronized Connection getConnection(String sessionId){
		setLogLevel();
		Connection conGeneral = null;
		conGeneral = poolConnector.getConnection(sessionId);
		//printAllConnectionCount(System.out);
		LOGGER.debug("get connection:"+conGeneral+" for session: "+sessionId);
		updateErrorInformation(conGeneral);
		return conGeneral;
	}
	

	/** first attempt to retrieve connection form new ( unique ) HTTP.SESSION.ID 
	 * */
	public static synchronized Connection getConnection(String moduleName, 
										   String userName, 
										   String password, 
										   String sessionId, 
										   String language,
										   String lastConnectionIp, 
										   String SMSConfirmCode){

		setLogLevel();
		LOGGER.debug("Connector.getConnection('"+moduleName+"','"+userName+"','"+/*password+"','"+*/sessionId+"','"+language+"','"+lastConnectionIp+"','"+SMSConfirmCode+"')");
		Connection returnValue=
		poolConnector.getConnection(moduleName, 
				   						   userName, 
										   password, 
										   sessionId, 
										   language,
										   lastConnectionIp, 
										   SMSConfirmCode);
		LOGGER.debug("Connector.getConnection  ("+returnValue+"), sessionId=" + sessionId);
		updateErrorInformation(returnValue);
		return  returnValue;
	}
	
	/**
	 * for internal using only, get connection for "system" user
	 * @param sessionId
	 * @return
	 */
	public static synchronized Connection getAdminConnection(String language){
		setLogLevel();
		LOGGER.debug("Connector.getAdminConnection ");
		Connection returnValue=
		poolConnector.getConnection(null,
				                    SYSTEM_LOGIN, 
									SYSTEM_PASSWORD, 
									SCHEMA_QUERY_SESSION,
									language,
									null,
									null);
		LOGGER.debug("Connector.getAdminConnection  ("+returnValue+"), sessionId=" + SCHEMA_QUERY_SESSION);
		updateErrorInformation(returnValue);
		return  returnValue;
	}
	
	/**
	 * set or clear error information, depends on input parameter
	 * @param returnValue
	 */
	private static void updateErrorInformation(Connection returnValue){
		//if (returnValue==null) {
			errorCode.set(poolConnector.getErrorCode());
			errorMessage.set(poolConnector.getErrorMessage());
		//}else{
		//	errorCode.remove();
		//	errorMessage.remove();
		//}
	}

	/** return Connection to POOL */
	public static void closeConnection(Connection connection){
		setLogLevel();
		if(connection==null){
			return;
		}
		LOGGER.debug("Connector.closeConnection("+connection+")");
		UtilityConnector.closeQuietly(connection);
	}
	
	/** remove session id from "known", next connection should be provided ( for this sessionId ) by {@link #getConnection(String, String, String, String, String, String)} */
	public static void removeSessionId(String sessionId){
		setLogLevel();
		LOGGER.debug("Connector.removeSessionId("+sessionId+")");
		poolConnector.removeSessionId(sessionId);
	}

	/**
	 * print count of all connections in the system 
	 * @param out
	 */
	public static void printAllConnectionCount(PrintStream out){
		// poolConnector.printConnection(out);
	}
	
	public static Map<String, String> getConnectionMap(){
		// return poolConnector.getConnectionMap();
		return new HashMap<String, String>();
	}
	
	/** удалить по указанному пользователю все соединения с базой*/
	public static void dropUserConnection(String userName){
		poolConnector.removeSessionsByUser(userName);
	}
	 
}
