package bc.connection;

import java.sql.Connection;

public interface SessionConnector {
	
	Connection getConnection(
			String moduleName, 
			String userName, 
			String password, 
			String sessionId, 
			String language,
			String lastConnectionIp, 
			String SMSConfirmCode);

	void closeConnection(Connection connection);
	
	Connection getConnection(String sessionId);
	
	void removeSessionId(String sessionId);
	
	void removeSessionsByUser(String userId);
	
	int getErrorCode();
	String getErrorMessage();
	
}
